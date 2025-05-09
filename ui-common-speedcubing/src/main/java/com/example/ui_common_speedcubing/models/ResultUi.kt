package com.example.ui_common_speedcubing.models

import kotlinx.serialization.Serializable

@Serializable
data class ResultUi(
    val userName: String,
    val time: Long,
    val penalty: PenaltyUi
)