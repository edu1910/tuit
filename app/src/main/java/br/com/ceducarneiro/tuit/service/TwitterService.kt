package br.com.ceducarneiro.tuit.service

import br.com.ceducarneiro.tuit.BuildConfig
import br.com.ceducarneiro.tuit.model.SearchTweetsResponse
import br.com.ceducarneiro.tuit.model.Tweet
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface TwitterService {
    @GET("search/tweets.json")
    suspend fun searchTweets(
        @Query("q") query: String,
        @Query("max_id") maxId: String?,
        @Query("count") pageSize: Int?,
        @Header("Authorization") token: String
    ): SearchTweetsResponse

    @GET("statuses/user_timeline.json")
    suspend fun userTimeline(
        @Query("screen_name") username: String,
        @Query("max_id") maxId: String?,
        @Query("count") pageSize: Int?,
        @Header("Authorization") token: String
    ): List<Tweet>

    companion object {
        fun create(): TwitterService {
            val logger =
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TwitterService::class.java)
        }
    }
}