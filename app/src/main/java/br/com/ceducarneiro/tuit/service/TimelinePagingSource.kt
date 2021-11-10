package br.com.ceducarneiro.tuit.service

import androidx.paging.PagingSource
import androidx.paging.PagingState
import br.com.ceducarneiro.tuit.BuildConfig
import br.com.ceducarneiro.tuit.model.Tweet
import retrofit2.HttpException
import java.io.IOException
import java.lang.Long


class TimelinePagingSource(
    private val service: TwitterService,
    private val username: String,
    private val pageSize: Int
) : PagingSource<String, Tweet>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Tweet> {
        try {
            val sinceId = params.key
            val response = service.userTimeline(
                username,
                sinceId,
                pageSize,
                "Bearer ${BuildConfig.TWITTER_TOKEN}"
            )

            var next: String? = null
            if (response.isNotEmpty()) {
                next = (Long.valueOf(response.last().id!!) + 1).toString()
            }

            return LoadResult.Page(
                data = response,
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