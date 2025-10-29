package com.kerymov.data_profile.models

data class UserResults(
    val bestResultsByPuzzle: List<BestResult>,
    val allResultsByPuzzle: List<Solve>
)
