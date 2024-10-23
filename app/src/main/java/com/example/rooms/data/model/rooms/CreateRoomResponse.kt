package com.example.rooms.data.model.rooms

data class CreateRoomResponse(
    val errorMessage: String?,
    val isSuccess: Boolean,
    val model: Model,
    val statusCode: Int
)