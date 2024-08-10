package com.example.rooms.data.model.rooms

data class RoomSettingsDto(
    val puzzle: Int,
    val isOpen: Boolean = true,
    val enableSolveTimeLimit: Boolean = false,
    val usersLimit: Int = 100
)