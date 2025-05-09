package com.example.domain_room.models

import com.example.domain_common_speedcubing.models.Penalty

data class NewSolveResult(
    val roomId: String,
    val solveNumber: Int,
    val timeInMilliseconds: Long,
    val penalty: Penalty
)
