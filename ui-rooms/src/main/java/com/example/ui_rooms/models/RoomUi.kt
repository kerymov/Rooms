package com.example.ui_rooms.models

import com.example.ui_common_speedcubing.models.EventUi

data class RoomUi(
    val id: String,
    val name: String,
    val event: EventUi,
    val administratorName: String?,
    val connectedUsersCount: Int,
    val maxUsersCount: Int,
    val isOpen: Boolean
)
