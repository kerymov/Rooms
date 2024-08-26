package com.example.rooms.domain.useCases.auth

import com.example.rooms.domain.model.BaseResult
import com.example.rooms.domain.model.User
import com.example.rooms.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow

class SignInUseCase(
    private val repository: AccountRepository
) {

    suspend fun invoke(username: String, password: String): Flow<BaseResult<User>> {
        return repository.signIn(username, password)
    }
}