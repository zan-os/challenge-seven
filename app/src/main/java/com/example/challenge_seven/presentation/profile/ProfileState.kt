package com.example.challenge_seven.presentation.profile

import com.example.challenge_seven.domain.model.User

data class ProfileState(
    val isLoading: Boolean = false,
    val error: String = "",
    val user: User? = null,
    val result: Int? = null
)