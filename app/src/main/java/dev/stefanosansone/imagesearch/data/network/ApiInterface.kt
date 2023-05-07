package dev.stefanosansone.imagesearch.data.network

import dev.stefanosansone.imagesearch.data.network.response.ImageResponse
import dev.stefanosansone.imagesearch.utils.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

/**
 * An interface representing the API endpoint for fetching user data.
 */
@Singleton
interface ApiInterface {
    @GET("?key=${API_KEY}")
    suspend fun getImages(
        @Query("page") page: Int,
        @Query("q") q: String
    ): ImageResponse
}