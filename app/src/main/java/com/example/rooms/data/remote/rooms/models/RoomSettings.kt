package com.example.rooms.data.remote.rooms.models

data class RoomSettings(
    val puzzle: Int,
    val isOpen: Boolean,
    val enableSolveTimeLimit: Boolean,
    val usersLimit: Int
)