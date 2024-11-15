package com.example.rooms.data.model.rooms

data class SolveDto(
    val solveNumber: Int,
    val scramble: String,
    val scrambledPuzzleImage: ScrambleDto.Image?,
    val results: List<ResultDto>
)