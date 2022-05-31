package com.example.challenge_seven.presentation.detailmovie

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.challenge_seven.common.Constant
import com.example.challenge_seven.common.Constant.TAG
import com.example.challenge_seven.common.Resource
import com.example.challenge_seven.domain.model.Favorite
import com.example.challenge_seven.domain.model.Movie
import com.example.challenge_seven.domain.usecase.addfavorite.AddFavoriteUseCase
import com.example.challenge_seven.domain.usecase.deletefavorite.DeleteFavoriteUseCase
import com.example.challenge_seven.domain.usecase.detailmovie.GetDetailMovieUseCase
import com.example.challenge_seven.domain.usecase.observefavorite.ObserveFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class DetailMovieViewModel @Inject constructor(
    private val detailUseCase: GetDetailMovieUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val observeFavoriteUseCase: ObserveFavoriteUseCase,
    private val deleteFavoriteUseCase: DeleteFavoriteUseCase
) : ViewModel() {

    private var _state = MutableLiveData(DetailMovieState())
    val state: LiveData<DetailMovieState> = _state

    private val _favoriteMovie = MutableLiveData<Favorite?>()
    val favoriteMovie: LiveData<Favorite?> = _favoriteMovie

    fun getMovie(movieId: Int) {
        Log.d(TAG, "DetailviewModel -> getMovie executed")
        Log.d(TAG, "DetailviewModel -> $movieId")
        detailUseCase(movieId).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = DetailMovieState(movie = result.data)
                    Log.d(TAG, "Detail ViewModel -> ${result.data}")
                }
                is Resource.Error -> {
                    _state.value =
                        DetailMovieState(
                            error = result.message ?: "An unexpected error occured"
                        )

                    Log.d(TAG, "Error ViewModel -> ${result.message}")
                }
                is Resource.Loading -> {
                    _state.value = DetailMovieState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun addFavoriteMovie(movie: Movie) {
        addFavoriteUseCase(movie).launchIn(viewModelScope)
    }

    fun observeFavoriteMovie(userId: Int, movieId: Int) {
        observeFavoriteUseCase(userId, movieId).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _favoriteMovie.value = result.data
                    Log.d(Constant.TAG, "Observe Favorite ViewModel -> ${result.data}")
                }
                is Resource.Error -> {
                    _state.value =
                        DetailMovieState(
                            error = result.message ?: "An unexpected error occured"
                        )

                    Log.d(Constant.TAG, "Error ViewModel -> ${result.message}")
                }
                is Resource.Loading -> {
                    _state.value = DetailMovieState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun deleteFavoriteMovie(userId: Int, movieId: Int?) {
        deleteFavoriteUseCase(userId, movieId).launchIn(viewModelScope)
    }
}