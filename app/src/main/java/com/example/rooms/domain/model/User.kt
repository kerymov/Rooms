package com.example.rooms.domain.model

data class User(
    val username: String,
    val token: String,
    val expiresIn: Int,
)
