package com.kerymov.data_onboarding.models

data class UserSignInRequest(
    val login: String,
    val password: String
)