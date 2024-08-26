package com.example.rooms.domain.useCases.auth

import com.example.rooms.domain.model.BaseResult
import com.example.rooms.domain.model.User
import com.example.rooms.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow

class GetUserUseCase(
    private val repository: AccountRepository
) {

    suspend fun invoke(): Flow<BaseResult<User>> {
        return repository.getUser()
    }
}