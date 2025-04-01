package com.example.data_rooms.models

import kotlinx.serialization.Serializable

@Serializable
data class ResultDto(
    val userName: String,
    val time: Long,
    val penalty: Int
)