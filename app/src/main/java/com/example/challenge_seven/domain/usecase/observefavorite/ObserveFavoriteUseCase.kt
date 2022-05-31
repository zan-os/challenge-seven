package com.example.challenge_seven.domain.usecase.observefavorite

import com.example.challenge_seven.common.Resource
import com.example.challenge_seven.domain.model.Favorite
import com.example.challenge_seven.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ObserveFavoriteUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {
    operator fun invoke(userId: Int, movieId: Int): Flow<Resource<Favorite?>> = flow {
        try {
            emit(Resource.Loading())
            val movie = repository.observeFavoriteMovie(userId, movieId)
            emit(Resource.Success(movie))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        }
    }
}