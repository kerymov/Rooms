package com.example.ui_rooms.models

data class NewSolveResultUi(
    val roomId: String,
    val solveNumber: Int,
    val timeInMilliseconds: Long,
    val penalty: PenaltyUi
)
