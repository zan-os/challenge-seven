package com.example.challenge_seven.data.remote

import com.example.challenge_seven.data.remote.dto.DetailDto
import com.example.challenge_seven.data.remote.dto.ResultDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDbApi {

    @GET("movie/popular")
    suspend fun getMovies(
        @Query("api_key") apiKey: String
    ): ResultDto

    @GET("movie/{movie_id}")
    suspend fun getMovieById(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): DetailDto
}