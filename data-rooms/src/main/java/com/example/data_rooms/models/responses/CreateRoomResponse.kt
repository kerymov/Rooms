package com.example.data_rooms.models.responses

import com.example.data_rooms.models.CreateRoomDetailsDto
import com.google.gson.annotations.SerializedName

data class CreateRoomResponse(
    val isSuccess: Boolean,
    val statusCode: Int,
    val errorMessage: String?,

    @SerializedName("model")
    val roomDetails: CreateRoomDetailsDto,
)