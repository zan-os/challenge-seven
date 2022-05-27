package com.example.challenge_seven.data.repository

import com.example.challenge_seven.data.local.entity.FavoriteEntity
import com.example.challenge_seven.data.local.entity.toFavorite
import com.example.challenge_seven.data.local.entity.toMovie
import com.example.challenge_seven.data.local.room.dao.FavoriteDao
import com.example.challenge_seven.domain.model.Favorite
import com.example.challenge_seven.domain.repository.FavoriteRepository
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteDao: FavoriteDao
) : FavoriteRepository {

    override suspend fun addFavoriteMovie(movie: FavoriteEntity) {
        return favoriteDao.addFavoriteMovie(movie)
    }

    override suspend fun deleteFavoriteMovie(userId: Int, movieId: Int?) {
        return favoriteDao.removeFavoriteMovie(userId, movieId)
    }

    override suspend fun observeFavoriteMovie(userId: Int, movieId: Int): Favorite? {
        return favoriteDao.observeFavoriteMovie(userId, movieId)?.toFavorite()
    }

    override suspend fun observeFavoriteMovieById(userId: Int): List<FavoriteEntity> {
        return favoriteDao.observerFavoriteMovieById(userId)
    }
}