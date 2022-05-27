package com.example.challenge_seven.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.challenge_seven.domain.model.Favorite
import com.example.challenge_seven.domain.model.Movie

@Entity(tableName = "favorite_movies")
data class FavoriteEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "movie_id")
    val movieId: Int? = 0,

    @ColumnInfo(name = "user_id")
    val userId: Int? = 0,

    @ColumnInfo(name = "title")
    val title: String?,

    @ColumnInfo(name = "poster_path")
    val posterPath: String?,

    @ColumnInfo(name = "popularity")
    val popularity: Double? = 0.0,

    @ColumnInfo(name = "vote_average")
    val voteAverage: Double? = 0.0,
)

fun FavoriteEntity.toFavorite(): Favorite =
    Favorite(
        id, movieId, userId, title, posterPath, popularity, voteAverage
    )

fun FavoriteEntity.toMovie(): Movie =
    Movie(
        id = id,
        popularity = popularity,
        posterPath = posterPath,
        title = title,
        voteAverage = voteAverage
    )
