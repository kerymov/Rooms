package com.example.rooms.presentation.features.auth.models

data class UserUiModel(
    val name: String,
    val token: String,
    val expiresIn: Int,
)
