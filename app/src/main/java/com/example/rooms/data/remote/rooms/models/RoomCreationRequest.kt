package com.example.rooms.data.remote.rooms.models

data class RoomCreationRequest(
    val roomName: String,
    val roomPassword: String,
    val settings: RoomSettings
)
