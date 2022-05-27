package com.example.challenge_seven.presentation.favorite

import com.example.challenge_seven.domain.model.Movie

data class FavoriteState(
    val isLoading: Boolean = false,
    val error: String = "",
    val movies: List<Movie>? = null
)
