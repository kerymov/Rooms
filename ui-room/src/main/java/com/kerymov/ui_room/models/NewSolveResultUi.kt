package com.kerymov.ui_room.models

import com.kerymov.ui_common_speedcubing.models.PenaltyUi

data class NewSolveResultUi(
    val roomId: String,
    val solveNumber: Int,
    val timeInMilliseconds: Long,
    val penalty: PenaltyUi
)
