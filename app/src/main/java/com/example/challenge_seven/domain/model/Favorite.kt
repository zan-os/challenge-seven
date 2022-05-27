package com.example.challenge_seven.domain.model

data class Favorite(
    val id: Int? = 0,
    val movieId: Int? = 0,
    val userId: Int? = 0,
    val title: String?,
    val posterPath: String?,
    val popularity: Double? = 0.0,
    val voteAverage: Double? = 0.0,
)
