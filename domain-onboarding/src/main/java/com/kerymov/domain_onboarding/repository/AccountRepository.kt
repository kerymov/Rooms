package com.kerymov.domain_onboarding.repository

import com.kerymov.domain_core.utils.BaseResult

interface AccountRepository {

    suspend fun signIn(username: String, password: String): BaseResult<Unit>

    suspend fun signUp(username: String, password: String, passwordConfirm: String): BaseResult<Unit>
}