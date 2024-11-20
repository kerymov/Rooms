package com.example.rooms.domain.model.auth

data class User(
    val username: String,
    val token: String,
    val expiresIn: Int,
)
