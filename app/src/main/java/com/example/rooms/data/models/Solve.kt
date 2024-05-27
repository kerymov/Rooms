package com.example.rooms.data.models

data class Solve(
    val solveNumber: Int,
    val scramble: String,
    val startTime: String,
    val results: List<Result>
)