package com.example.rooms.data.model.rooms

data class CreateRoomRequest(
    val roomName: String,
    val roomPassword: String?,
    val settings: RoomSettingsDto
)
