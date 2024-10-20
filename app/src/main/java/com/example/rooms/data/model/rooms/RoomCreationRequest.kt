package com.example.rooms.data.model.rooms

data class RoomCreationRequest(
    val roomName: String,
    val roomPassword: String,
    val settings: RoomSettingsDto
)
