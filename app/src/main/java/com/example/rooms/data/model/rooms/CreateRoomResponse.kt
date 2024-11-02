package com.example.rooms.data.model.rooms

import com.google.gson.annotations.SerializedName

data class CreateRoomResponse(
    val isSuccess: Boolean,
    val statusCode: Int,
    val errorMessage: String?,

    @SerializedName("model")
    val roomDetails: RoomDetailsDto,
)