package com.example.rooms.data.models

data class UserLoginResponse(
    val errorMessage: String?,
    val expiresIn: Int,
    val isSuccess: Boolean,
    val statusCode: Int,
    val token: String,
    val username: String
)