package com.example.rooms.data.model.rooms

data class NewSolveResultDto(
    val roomId: String,
    val solveNumber: Int,
    val timeInMilliseconds: Int,
    val penalty: Int
)

