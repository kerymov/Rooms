package com.example.domain_rooms.models

data class Solve(
    val solveNumber: Int,
    val scramble: Scramble,
    val results: List<Result>
)