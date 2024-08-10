package com.example.rooms.data.model.account.auth

data class UserAuthResponseDto(
    val errorMessage: String?,
    val expiresIn: Int,
    val isSuccess: Boolean,
    val statusCode: Int,
    val token: String,
    val username: String
)