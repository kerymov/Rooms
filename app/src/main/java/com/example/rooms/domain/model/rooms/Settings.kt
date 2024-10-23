package com.example.rooms.domain.model.rooms

data class Settings(
    val event: Event,
    val isOpen: Boolean = true,
    val enableSolveTimeLimit: Boolean = false,
    val usersLimit: Int = 100
)