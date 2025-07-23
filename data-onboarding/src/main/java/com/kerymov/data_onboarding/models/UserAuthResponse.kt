package com.kerymov.data_onboarding.models

data class UserAuthResponse(
    val errorMessage: String?,
    val expiresIn: Int,
    val isSuccess: Boolean,
    val statusCode: Int,
    val token: String?,
    val username: String?
)