package com.example.rooms.data.remote.rooms.models

data class RoomSettings(
    val puzzle: Int,
    val isOpen: Boolean = true,
    val enableSolveTimeLimit: Boolean = false,
    val usersLimit: Int = 100
)