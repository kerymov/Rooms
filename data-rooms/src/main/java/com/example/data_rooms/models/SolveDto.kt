package com.example.data_rooms.models

@Serializable
data class SolveDto(
    val solveNumber: Int,
    val scramble: String,
    val scrambledPuzzleImage: ScrambleDto.Image?,
    val results: List<ResultDto>
)