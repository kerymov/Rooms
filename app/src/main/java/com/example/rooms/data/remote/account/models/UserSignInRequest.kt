package com.example.rooms.data.remote.account.models

data class UserSignInRequest(
    val login: String,
    val password: String
)