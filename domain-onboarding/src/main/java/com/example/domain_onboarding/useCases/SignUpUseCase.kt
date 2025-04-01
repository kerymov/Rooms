package com.example.domain_onboarding.useCases

import com.example.domain_core.utils.BaseResult
import com.example.domain_onboarding.repository.AccountRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val repository: AccountRepository
) {

    suspend fun invoke(username: String, password: String, passwordConfirm: String): BaseResult<Unit> {
        return repository.signUp(username, password, passwordConfirm)
    }
}