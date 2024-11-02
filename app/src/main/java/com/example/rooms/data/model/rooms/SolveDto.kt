package com.example.rooms.data.model.rooms

data class SolveDto(
    val solveNumber: Int,
    val scramble: ScrambleDto,
    val results: List<ResultDto>
)