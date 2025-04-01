package com.example.data_rooms.models.requests

data class LoginRoomRequest(
    val roomName: String,
    val roomPassword: String?,
)
