package com.example.challenge_seven.domain.usecase.observefavorite

import com.example.challenge_seven.common.Resource
import com.example.challenge_seven.data.local.entity.toMovie
import com.example.challenge_seven.domain.model.Movie
import com.example.challenge_seven.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ObserveFavoriteMovieByIdUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {
    operator fun invoke(userId: Int): Flow<Resource<List<Movie>>> = flow {
        try {
            emit(Resource.Loading())
            val movie = repository.observeFavoriteMovieById(userId).map { it.toMovie() }
            emit(Resource.Success(movie))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        }
    }
}