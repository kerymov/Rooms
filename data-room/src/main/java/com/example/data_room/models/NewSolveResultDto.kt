package com.example.data_room.models

data class NewSolveResultDto(
    val roomId: String,
    val solveNumber: Int,
    val timeInMilliseconds: Int,
    val penalty: Int
)

