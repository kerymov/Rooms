package com.kerymov.domain_common_speedcubing.models

data class Solve(
    val solveNumber: Int,
    val scramble: Scramble,
    val results: List<Result>
)