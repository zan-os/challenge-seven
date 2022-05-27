package com.example.challenge_seven.presentation.movie_list

import com.example.challenge_seven.domain.model.Movie
import com.example.challenge_seven.domain.model.User

data class MovieListState(
    val isLoading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val error: String = "",
    val user: User? = null
)
