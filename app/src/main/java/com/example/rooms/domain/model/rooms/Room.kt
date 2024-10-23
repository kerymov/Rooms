package com.example.rooms.domain.model.rooms

data class Room(
    val id: String,
    val name: String,
    val event: Int,
    val administratorName: String,
    val connectedUsersCount: Int,
    val maxUsersCount: Int,
    val isOpen: Boolean
)