package com.example.data_rooms.models

data class RoomDto(
    val id: String,
    val roomName: String,
    val puzzle: Int,
    val administratorName: String,
    val connectedUsersCount: Int,
    val maxUsersCount: Int,
    val isOpen: Boolean
)