package com.example.data_rooms.models.responses

import com.example.data_rooms.models.LoginRoomDetailsDto
import com.google.gson.annotations.SerializedName

data class LoginRoomResponse(
    val isSuccess: Boolean,
    val statusCode: Int,
    val errorMessage: String?,

    @SerializedName("model")
    val roomDetails: LoginRoomDetailsDto,
)
