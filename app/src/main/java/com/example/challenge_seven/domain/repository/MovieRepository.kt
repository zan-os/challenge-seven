package com.example.challenge_seven.domain.repository

import com.example.challenge_seven.data.remote.dto.DetailDto
import com.example.challenge_seven.data.remote.dto.MovieDto

interface MovieRepository {

    suspend fun getMovies(): List<MovieDto>

    suspend fun getMovieById(movieId: Int): DetailDto
}