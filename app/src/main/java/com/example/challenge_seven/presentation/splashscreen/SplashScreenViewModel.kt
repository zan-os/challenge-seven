package com.example.challenge_seven.presentation.splashscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.challenge_seven.data.local.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    preferences: UserPreferences
) : ViewModel() {
    val email = preferences.getEmail().asLiveData()
}