package com.example.domain_onboarding.useCases

import com.example.domain_core.utils.BaseResult
import com.example.domain_onboarding.repository.AccountRepository

class SignInUseCase(
    private val repository: AccountRepository
) {

    suspend fun invoke(username: String, password: String): BaseResult<Unit> {
        return repository.signIn(username, password)
    }
}