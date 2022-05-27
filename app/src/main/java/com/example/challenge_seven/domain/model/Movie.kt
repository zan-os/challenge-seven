package com.example.challenge_seven.domain.model

import android.os.Parcelable
import com.example.challenge_seven.data.local.entity.FavoriteEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val id: Int,
    var userId: Int = 0,
    val popularity: Double?,
    val posterPath: String?,
    val title: String?,
    val voteAverage: Double?,
) : Parcelable

fun Movie.toFavoriteEntity(): FavoriteEntity =
    FavoriteEntity(
        id,
        movieId = id,
        userId,
        title,
        posterPath,
        popularity,
        voteAverage
    )
