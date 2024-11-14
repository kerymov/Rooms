package com.example.rooms.presentation.features.main.rooms.models

import kotlinx.serialization.Serializable

@Serializable
data class ResultUi(
    val userName: String,
    val time: Long,
    val penalty: PenaltyUi
)