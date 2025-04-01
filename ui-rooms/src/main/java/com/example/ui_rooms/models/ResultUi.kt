package com.example.ui_rooms.models

import kotlinx.serialization.Serializable

@Serializable
data class ResultUi(
    val userName: String,
    val time: Long,
    val penalty: PenaltyUi
)