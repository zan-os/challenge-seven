package com.example.challenge_seven.domain.repository

import com.example.challenge_seven.data.local.entity.UserEntity

interface AuthRepository {

    suspend fun login(email: String, password: String): UserEntity

    suspend fun register(user: UserEntity): Long

    suspend fun getUserData(email: String): UserEntity

    suspend fun updateUser(user: UserEntity): Int
}