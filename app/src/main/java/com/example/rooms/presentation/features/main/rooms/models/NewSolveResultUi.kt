package com.example.rooms.presentation.features.main.rooms.models

data class NewSolveResultUi(
    val roomId: String,
    val solveNumber: Int,
    val timeInMilliseconds: Long,
    val penalty: PenaltyUi
)
