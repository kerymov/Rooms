package com.kerymov.data_rooms.models.requests

data class LoginRoomRequest(
    val roomName: String,
    val roomPassword: String?,
)
