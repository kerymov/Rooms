package com.kerymov.domain_room.models

import com.kerymov.domain_common_speedcubing.models.Penalty

data class NewSolveResult(
    val roomId: String,
    val solveNumber: Int,
    val timeInMilliseconds: Long,
    val penalty: Penalty
)
