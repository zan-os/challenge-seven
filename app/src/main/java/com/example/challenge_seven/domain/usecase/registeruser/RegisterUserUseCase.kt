package com.example.challenge_seven.domain.usecase.registeruser

import com.example.challenge_seven.common.Resource
import com.example.challenge_seven.domain.model.User
import com.example.challenge_seven.domain.model.toUserEntity
import com.example.challenge_seven.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(user: User): Flow<Resource<Long>> = flow {
        try {
            emit(Resource.Loading())
            val userEntity = user.toUserEntity()
            val data = repository.register(userEntity)
            emit(Resource.Success(data))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        }
    }
}