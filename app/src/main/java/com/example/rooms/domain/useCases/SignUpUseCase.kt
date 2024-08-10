package com.example.rooms.domain.useCases

import com.example.rooms.domain.model.BaseResult
import com.example.rooms.domain.model.User
import com.example.rooms.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow

class SignUpUseCase(
    private val repository: AccountRepository
) {

    suspend fun invoke(username: String, password: String, passwordConfirm: String): Flow<BaseResult<User>> {
        return repository.signUp(username, password, passwordConfirm)
    }
}