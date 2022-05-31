package com.example.challenge_seven.presentation.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.challenge_seven.common.Constant
import com.example.challenge_seven.common.Resource
import com.example.challenge_seven.data.local.UserPreferences
import com.example.challenge_seven.domain.model.User
import com.example.challenge_seven.domain.usecase.getuserdata.GetUserDataUseCase
import com.example.challenge_seven.domain.usecase.updateuser.UpdateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val updateUserUseCase: UpdateUserUseCase,
    private val credentialUseCase: GetUserDataUseCase,
    private val preferences: UserPreferences
) : ViewModel() {

    private val _userData = MutableLiveData(ProfileState())
    val userData: LiveData<ProfileState> = _userData

    private val _updateResult = MutableLiveData(ProfileState())
    val updateResult: LiveData<ProfileState> = _updateResult

    val email = preferences.getEmail().asLiveData()

    fun getUser(email: String) {
        credentialUseCase(email).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _userData.postValue(ProfileState(user = result.data))
                    Log.d(Constant.TAG, "Login ViewModel -> ${result.data}")
                }
                is Resource.Error -> {
                    _userData.postValue(
                        ProfileState(
                            error = result.message ?: "An unexpected error occured"
                        )
                    )
                    Log.d(Constant.TAG, "Error ViewModel -> ${result.message}")
                }
                is Resource.Loading -> {
                    _userData.postValue(ProfileState(isLoading = true))
                }
            }
        }.launchIn(CoroutineScope(Dispatchers.IO))
    }

    fun updateUser(user: User) {
        updateUserUseCase(user).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _updateResult.postValue(ProfileState(result = result.data))
                    Log.d(Constant.TAG, "Update ViewModel -> ${result.data}")
                }
                is Resource.Error -> {
                    _updateResult.postValue(
                        ProfileState(
                            error = result.message ?: "An unexpected error occured"
                        )
                    )
                    Log.d(Constant.TAG, "Error Update ViewModel -> ${result.message}")
                }
                is Resource.Loading -> {
                    _updateResult.postValue(ProfileState(isLoading = true))
                }
            }
        }.launchIn(CoroutineScope(Dispatchers.IO))
    }

    fun logout() {
        preferences.logout()
    }
}