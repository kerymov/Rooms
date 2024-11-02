package com.example.rooms.presentation.features.main.rooms.models

data class SolveUi(
    val solveNumber: Int,
    val scramble: ScrambleUi,
    val results: List<ResultUi>
)