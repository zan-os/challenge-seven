package com.example.challenge_seven.presentation.register

data class RegisterState(
    val isLoading: Boolean = false,
    val user: Long? = null,
    val error: String = ""
)
