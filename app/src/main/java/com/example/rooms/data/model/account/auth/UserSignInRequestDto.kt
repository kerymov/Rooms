package com.example.rooms.data.model.account.auth

data class UserSignInRequestDto(
    val login: String,
    val password: String
)