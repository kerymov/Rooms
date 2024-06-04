package com.example.rooms.data.remote.rooms.models

data class Room(
    val id: String,
    val roomName: String,
    val puzzle: Int,
    val administratorName: String,
    val connectedUsersCount: Int,
    val maxUsersCount: Int,
    val isOpen: Boolean
)