package br.com.ceducarneiro.tuit.service

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import br.com.ceducarneiro.tuit.model.Tweet
import br.com.ceducarneiro.tuit.model.Tweet.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TwitterRepository @Inject constructor(
    private val service: TwitterService
) {
    fun searchTweets(query: String): Flow<PagingData<Tweet>> {
        return Pager(
            config = PagingConfig(
                enablePlaceholders = true,
                pageSize = NETWORK_PAGE_SIZE,
            ),
            pagingSourceFactory = { TweetPagingSource(service, query, NETWORK_PAGE_SIZE) }
        ).flow
    }

    fun userTimeline(user: User): Flow<PagingData<Tweet>> {
        return Pager(
            config = PagingConfig(
                enablePlaceholders = true,
                pageSize = NETWORK_PAGE_SIZE,
            ),
            pagingSourceFactory = {
                TimelinePagingSource(
                    service,
                    user.username!!,
                    NETWORK_PAGE_SIZE
                )
            }
        ).flow
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 50
    }
}