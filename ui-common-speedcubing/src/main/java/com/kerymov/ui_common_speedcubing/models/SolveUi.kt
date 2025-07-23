package com.kerymov.ui_common_speedcubing.models

import kotlinx.serialization.Serializable

@Serializable
data class SolveUi(
    val solveNumber: Int,
    val scramble: ScrambleUi,
    val results: List<ResultUi>
)