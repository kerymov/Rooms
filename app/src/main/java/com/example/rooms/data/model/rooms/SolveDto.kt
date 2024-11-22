package com.example.rooms.data.model.rooms

import kotlinx.serialization.Serializable

@Serializable
data class SolveDto(
    val solveNumber: Int,
    val scramble: String,
    val scrambledPuzzleImage: ScrambleDto.Image?,
    val results: List<ResultDto>
)