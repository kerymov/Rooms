package com.kerymov.data_rooms.models.responses

import com.kerymov.data_rooms.models.CreateRoomDetailsDto
import com.google.gson.annotations.SerializedName
import com.kerymov.network_core.AlwaysSuccessfulResponse

data class CreateRoomResponse(
    override val isSuccess: Boolean,
    override val statusCode: Int,
    override val errorMessage: String?,

    @SerializedName("model")
    val roomDetails: CreateRoomDetailsDto
) : AlwaysSuccessfulResponse