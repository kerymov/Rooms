package com.example.rooms.data.model.rooms.requests

import com.example.rooms.data.model.rooms.RoomSettingsDto

data class CreateRoomRequest(
    val roomName: String,
    val roomPassword: String?,
    val settings: RoomSettingsDto
)
