package com.example.challenge_seven.di

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
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideMovieRepository(api: TheMovieDbApi): MovieRepository {
        return MovieRepositoryImpl(api)
    }

    @Provides
    fun provideAuthRepository(userDao: UserDao): AuthRepository {
        return AuthRepositoryImpl(userDao)
    }

    @Provides
    fun provideFavoriteRepository(favoriteDao: FavoriteDao): FavoriteRepository {
        return FavoriteRepositoryImpl(favoriteDao)
    }
}