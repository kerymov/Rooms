package com.example.rooms.data.model.account.auth.network

data class UserSignUpRequestDto(
    val userName: String,
    val password: String,
    val passwordConfirm: String
)