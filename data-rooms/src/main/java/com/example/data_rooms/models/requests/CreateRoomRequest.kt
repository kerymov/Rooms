package com.example.data_rooms.models.requests

import com.example.data_rooms.models.RoomSettingsDto

data class CreateRoomRequest(
    val roomName: String,
    val roomPassword: String?,
    val settings: RoomSettingsDto
)
