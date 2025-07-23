package com.kerymov.data_rooms.models.requests

import com.kerymov.data_rooms.models.RoomSettingsDto

data class CreateRoomRequest(
    val roomName: String,
    val roomPassword: String?,
    val settings: RoomSettingsDto
)
