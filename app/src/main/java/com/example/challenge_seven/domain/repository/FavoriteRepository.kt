package com.example.challenge_seven.domain.repository

import com.example.challenge_seven.data.local.entity.FavoriteEntity
import com.example.challenge_seven.domain.model.Favorite

interface FavoriteRepository {

    suspend fun addFavoriteMovie(movie: FavoriteEntity)

    suspend fun deleteFavoriteMovie(userId: Int, movieId: Int?)

    suspend fun observeFavoriteMovie(userId: Int, movieId: Int): Favorite?

    suspend fun observeFavoriteMovieById(userId: Int): List<FavoriteEntity>
}