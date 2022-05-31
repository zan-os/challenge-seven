package com.example.challenge_seven.domain.usecase.detailmovie

import android.util.Log
import com.example.challenge_seven.common.Constant.TAG
import com.example.challenge_seven.common.Resource
import com.example.challenge_seven.data.remote.dto.toDetail
import com.example.challenge_seven.domain.model.Detail
import com.example.challenge_seven.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetDetailMovieUseCase @Inject constructor(
    private val repository: MovieRepository
) {

    operator fun invoke(movieId: Int): Flow<Resource<Detail>> = flow {
        Log.d(TAG, "Detail Usecase -> DetailUsecase executed")
        try {
            emit(Resource.Loading())
            val movie = repository.getMovieById(movieId).toDetail()
            Log.d(TAG, "Detail Usecase -> $movie")
            emit(Resource.Success(movie))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured."))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connectivity"))
        }
    }
}