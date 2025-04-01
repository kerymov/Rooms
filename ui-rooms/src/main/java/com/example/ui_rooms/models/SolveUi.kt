package com.example.ui_rooms.models

import kotlinx.serialization.Serializable

@Serializable
data class SolveUi(
    val solveNumber: Int,
    val scramble: ScrambleUi,
    val results: List<ResultUi>
)