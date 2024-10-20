package com.example.rooms.data.model.account.auth.network

data class UserSignInRequestDto(
    val login: String,
    val password: String
)