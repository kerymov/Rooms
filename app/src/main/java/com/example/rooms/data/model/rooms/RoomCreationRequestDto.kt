package com.example.rooms.data.model.rooms

data class RoomCreationRequestDto(
    val roomName: String,
    val roomPassword: String,
    val settings: RoomSettingsDto
)
