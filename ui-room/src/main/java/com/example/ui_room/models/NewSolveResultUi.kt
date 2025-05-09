package com.example.ui_room.models

import com.example.ui_common_speedcubing.models.PenaltyUi

data class NewSolveResultUi(
    val roomId: String,
    val solveNumber: Int,
    val timeInMilliseconds: Long,
    val penalty: PenaltyUi
)
