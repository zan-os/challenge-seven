package com.example.challenge_seven.presentation.detail_movie

import com.example.challenge_seven.domain.model.Detail
import com.example.challenge_seven.domain.model.Favorite

data class DetailMovieState(
    val isLoading: Boolean = false,
    val movie: Detail? = null,
    val error: String = "",
    val favoriteMovie: Unit? = null,
    val favoriteMovies: Favorite? = null
)