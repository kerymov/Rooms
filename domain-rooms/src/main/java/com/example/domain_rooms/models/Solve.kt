package com.example.domain_rooms.models

import com.example.domain_rooms.models.Result
import com.example.domain_rooms.models.Scramble

data class Solve(
    val solveNumber: Int,
    val scramble: Scramble,
    val results: List<Result>
)