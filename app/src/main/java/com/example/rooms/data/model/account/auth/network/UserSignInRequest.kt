package com.example.rooms.data.model.account.auth.network

data class UserSignInRequest(
    val login: String,
    val password: String
)