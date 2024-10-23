package com.example.rooms.domain.model.rooms

data class Solve(
    val solveNumber: Int,
    val scramble: Scramble,
    val results: List<Result>
)