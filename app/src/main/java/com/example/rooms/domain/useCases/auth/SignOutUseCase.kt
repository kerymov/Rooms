package com.example.rooms.domain.useCases.auth

import com.example.rooms.domain.repository.AccountRepository

class SignOutUseCase(
    private val repository: AccountRepository
) {

    suspend fun invoke() {
        repository.signOut()
    }
}