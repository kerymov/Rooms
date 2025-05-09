package com.example.data_common_speedcubing.models

data class SolveDto(
    val solveNumber: Int,
    val scramble: String,
    val scrambledPuzzleImage: ScrambleDto.Image?,
    val results: List<ResultDto>
)