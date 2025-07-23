package com.kerymov.domain_onboarding.useCases

import com.kerymov.domain_core.utils.BaseResult
import com.kerymov.domain_onboarding.repository.AccountRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val repository: AccountRepository
) {

    suspend fun invoke(username: String, password: String, passwordConfirm: String): BaseResult<Unit> {
        return repository.signUp(username, password, passwordConfirm)
    }
}