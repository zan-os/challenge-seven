package com.example.challenge_seven.presentation.detailmovie

import com.example.challenge_seven.domain.model.Detail
import com.example.challenge_seven.domain.model.Favorite

data class DetailMovieState(
    val isLoading: Boolean = false,
    val movie: Detail? = null,
    val error: String = "",
    val favoriteMovie: Unit? = null,
    val favoriteMovies: Favorite? = null
)