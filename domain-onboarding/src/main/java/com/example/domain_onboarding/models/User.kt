package com.example.domain_onboarding.models

data class User(
    val username: String,
    val token: String,
    val expiresIn: Int,
)
