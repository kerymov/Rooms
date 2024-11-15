package com.example.rooms.data.model.rooms.responses

import com.example.rooms.data.model.rooms.LoginRoomDetailsDto
import com.google.gson.annotations.SerializedName

data class LoginRoomResponse(
    val isSuccess: Boolean,
    val statusCode: Int,
    val errorMessage: String?,

    @SerializedName("model")
    val roomDetails: LoginRoomDetailsDto,
)
