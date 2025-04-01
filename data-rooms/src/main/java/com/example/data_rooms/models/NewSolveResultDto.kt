package com.example.data_rooms.models

data class NewSolveResultDto(
    val roomId: String,
    val solveNumber: Int,
    val timeInMilliseconds: Int,
    val penalty: Int
)

