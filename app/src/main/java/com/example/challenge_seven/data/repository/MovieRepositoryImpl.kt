package com.example.challenge_seven.data.repository

import com.example.challenge_seven.BuildConfig
import com.example.challenge_seven.data.remote.TheMovieDbApi
import com.example.challenge_seven.data.remote.dto.DetailDto
import com.example.challenge_seven.data.remote.dto.MovieDto
import com.example.challenge_seven.domain.repository.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val api: TheMovieDbApi
) : MovieRepository {

    override suspend fun getMovies(): List<MovieDto> {
        return api.getMovies(BuildConfig.API_KEY).result
    }

    override suspend fun getMovieById(movieId: Int): DetailDto {
        return api.getMovieById(movieId, BuildConfig.API_KEY)
    }
}