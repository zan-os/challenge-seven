package com.example.challenge_seven.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.challenge_seven.data.local.entity.FavoriteEntity
import com.example.challenge_seven.data.local.entity.UserEntity
import com.example.challenge_seven.data.local.room.dao.FavoriteDao
import com.example.challenge_seven.data.local.room.dao.UserDao
import com.example.challenge_seven.utils.Converters

@Database(entities = [UserEntity::class, FavoriteEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun favoriteDao(): FavoriteDao
}