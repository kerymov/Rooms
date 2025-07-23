package com.kerymov.domain_rooms.models

import com.kerymov.domain_common_speedcubing.models.Event

data class RoomSettings(
    val event: Event,
    val isOpen: Boolean = true,
    val enableSolveTimeLimit: Boolean = false,
    val usersLimit: Int = 100
)