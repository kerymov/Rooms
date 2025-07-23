package com.kerymov.data_onboarding.models

data class UserSignUpRequest(
    val userName: String,
    val password: String,
    val passwordConfirm: String
)