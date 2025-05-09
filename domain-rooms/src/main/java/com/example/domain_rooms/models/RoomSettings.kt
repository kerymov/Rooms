package com.example.domain_rooms.models

import com.example.domain_common_speedcubing.models.Event

data class RoomSettings(
    val event: Event,
    val isOpen: Boolean = true,
    val enableSolveTimeLimit: Boolean = false,
    val usersLimit: Int = 100
)