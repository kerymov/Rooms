package com.example.domain_rooms.models

data class NewSolveResult(
    val roomId: String,
    val solveNumber: Int,
    val timeInMilliseconds: Long,
    val penalty: Penalty
)
