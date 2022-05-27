package com.example.challenge_seven.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.challenge_seven.common.Constant.BASE_URL
import com.example.challenge_seven.data.local.UserPreferences
import com.example.challenge_seven.data.local.room.MovieDatabase
import com.example.challenge_seven.data.local.room.dao.FavoriteDao
import com.example.challenge_seven.data.local.room.dao.UserDao
import com.example.challenge_seven.data.remote.TheMovieDbApi
import com.example.challenge_seven.data.repository.AuthRepositoryImpl
import com.example.challenge_seven.data.repository.FavoriteRepositoryImpl
import com.example.challenge_seven.data.repository.MovieRepositoryImpl
import com.example.challenge_seven.domain.repository.AuthRepository
import com.example.challenge_seven.domain.repository.FavoriteRepository
import com.example.challenge_seven.domain.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MovieDatabase {
        return Room.databaseBuilder(context, MovieDatabase::class.java, "movie.db")
            .build()
    }

    @Provides
    fun provideUserDao(database: MovieDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    fun provideFavoriteDao(database: MovieDatabase): FavoriteDao {
        return database.favoriteDao()
    }
}