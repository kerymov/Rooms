package com.example.rooms.data.model.rooms

import kotlinx.serialization.Serializable

@Serializable
data class ResultDto(
    val userName: String,
    val time: Long,
    val penalty: Int
)