package com.example.rooms.data.remote.account.models

data class UserSignUpRequest(
    val userName: String,
    val password: String,
    val passwordConfirm: String
)