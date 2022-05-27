package com.example.challenge_seven.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.challenge_seven.data.local.entity.UserEntity

@Dao
interface UserDao {

    @Insert
    suspend fun register(user: UserEntity): Long

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    fun getUserData(email: String): UserEntity

    @Query("SELECT * FROM users WHERE email LIKE :email AND password LIKE :password")
    fun login(email: String, password: String): UserEntity

    @Update
    suspend fun update(user: UserEntity): Int

    @Query("SELECT * FROM users WHERE email LIKE :email")
    suspend fun validateEmail(email: String): UserEntity

    @Query("SELECT * FROM users WHERE username LIKE :username")
    suspend fun validateUsername(username: String): UserEntity
}