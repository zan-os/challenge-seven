package com.example.challenge_seven.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.challenge_seven.data.local.entity.FavoriteEntity

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavoriteMovie(movie: FavoriteEntity)

    @Query("SELECT * FROM favorite_movies WHERE user_id = :userId AND movie_id = :movieId")
    suspend fun observeFavoriteMovie(userId: Int, movieId: Int?): FavoriteEntity?

    @Query("SELECT * FROM favorite_movies WHERE user_id = :userId")
    suspend fun observerFavoriteMovieById(userId: Int): List<FavoriteEntity>

    @Query("DELETE FROM favorite_movies WHERE user_id = :userId AND movie_id = :movieId")
    suspend fun removeFavoriteMovie(userId: Int, movieId: Int?)
}