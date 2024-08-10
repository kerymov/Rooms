package com.example.rooms.data.model.rooms

data class RoomDto(
    val id: String,
    val roomName: String,
    val puzzle: Int,
    val administratorName: String,
    val connectedUsersCount: Int,
    val maxUsersCount: Int,
    val isOpen: Boolean
)