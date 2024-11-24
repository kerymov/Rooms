package com.example.rooms.domain.model.rooms

data class NewSolveResult(
    val roomId: String,
    val solveNumber: Int,
    val timeInMilliseconds: Long,
    val penalty: Penalty
)
