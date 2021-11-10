package br.com.ceducarneiro.tuit.service

import androidx.paging.PagingSource
import androidx.paging.PagingState
import br.com.ceducarneiro.tuit.BuildConfig
import br.com.ceducarneiro.tuit.model.Tweet
import retrofit2.HttpException
import java.io.IOException


class TweetPagingSource(
    private val service: TwitterService,
    private val query: String,
    private val pageSize: Int
) : PagingSource<String, Tweet>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Tweet> {
        try {
            val sinceId = params.key
            val response = service.searchTweets(
                query,
                sinceId,
                pageSize,
                "Bearer ${BuildConfig.TWITTER_TOKEN}"
            )
            val tweets = response.statuses

            var next: String? = null
            if (response.search_metadata?.next != null) {
                val result = "max_id=([0-9]+)".toRegex().find(response.search_metadata.next)
                next = result?.value?.replace("max_id=", "")
            }

            return LoadResult.Page(
                data = tweets,
                prevKey = null,
                nextKey = if (next != sinceId) next else null
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<String, Tweet>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}