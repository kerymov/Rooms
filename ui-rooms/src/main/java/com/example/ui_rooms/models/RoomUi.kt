package com.example.ui_rooms.models

data class RoomUi(
    val id: String,
    val name: String,
    val event: EventUi,
    val administratorName: String,
    val connectedUsersCount: Int,
    val maxUsersCount: Int,
    val isOpen: Boolean
)
