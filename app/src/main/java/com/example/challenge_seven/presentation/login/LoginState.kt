package com.example.challenge_seven.presentation.login

import com.example.challenge_seven.domain.model.User

data class LoginState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String = "",
    val result: Boolean = false
)