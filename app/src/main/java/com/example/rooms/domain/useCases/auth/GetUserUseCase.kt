package com.example.rooms.domain.useCases.auth

import com.example.rooms.domain.model.User
import com.example.rooms.domain.repository.AccountRepository

class GetUserUseCase(
    private val repository: AccountRepository
) {

    suspend fun invoke(): User? {
        return repository.getUser()
    }
}