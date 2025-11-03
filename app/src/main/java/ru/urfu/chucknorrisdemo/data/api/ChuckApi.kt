package ru.urfu.chucknorrisdemo.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ChuckApi {
    @GET("jokes/categories")
    suspend fun getCategories(): Response<List<String>>

    @GET("jokes/random")
    suspend fun getRandomJoke(): Response<ChuckJokeResponse>

    @GET("jokes/random")
    suspend fun getJokeByCategory(@Query("category") category: String): Response<ChuckJokeResponse>
}

data class ChuckJokeResponse(
    val value: String,
    val category: List<String>? = null
)