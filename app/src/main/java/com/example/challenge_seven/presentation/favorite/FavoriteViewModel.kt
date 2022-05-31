package com.example.challenge_seven.presentation.favorite

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.challenge_seven.common.Constant
import com.example.challenge_seven.common.Resource
import com.example.challenge_seven.domain.model.User
import com.example.challenge_seven.domain.usecase.getuserdata.GetUserDataUseCase
import com.example.challenge_seven.domain.usecase.observefavorite.ObserveFavoriteMovieByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val useCase: ObserveFavoriteMovieByIdUseCase,
    private val credentialUseCase: GetUserDataUseCase
) : ViewModel() {

    private var _state = MutableLiveData(FavoriteState())
    val state: LiveData<FavoriteState> = _state

    private var _userData = MutableLiveData<User?>()
    val userData: LiveData<User?> = _userData

    fun getMovie(userId: Int) {
        Log.d(Constant.TAG, "DetailviewModel -> getMovie executed")
        Log.d(Constant.TAG, "DetailviewModel -> $userId")
        useCase(userId).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = FavoriteState(movies = result.data)
                    Log.d(Constant.TAG, "Detail ViewModel -> ${result.data}")
                }
                is Resource.Error -> {
                    _state.value =
                        FavoriteState(
                            error = result.message ?: "An unexpected error occured"
                        )

                    Log.d(Constant.TAG, "Error ViewModel -> ${result.message}")
                }
                is Resource.Loading -> {
                    _state.value = FavoriteState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getUser(email: String) {
        credentialUseCase(email).onEach {
            _userData.postValue(it.data)
        }.launchIn(CoroutineScope(Dispatchers.IO))
    }
}