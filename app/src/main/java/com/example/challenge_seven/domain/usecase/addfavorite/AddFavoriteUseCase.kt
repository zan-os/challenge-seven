package com.example.challenge_seven.domain.usecase.addfavorite

import com.example.challenge_seven.common.Resource
import com.example.challenge_seven.domain.model.Movie
import com.example.challenge_seven.domain.model.toFavoriteEntity
import com.example.challenge_seven.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    operator fun invoke(movie: Movie): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            val favoriteEntity = movie.toFavoriteEntity()
            val data = favoriteRepository.addFavoriteMovie(favoriteEntity)
            emit(Resource.Success(data))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        }
    }

}