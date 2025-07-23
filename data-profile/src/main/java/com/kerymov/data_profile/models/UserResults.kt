package com.kerymov.rooms.data.model.account.results

data class UserResults(
    val bestResultsByPuzzle: List<BestResult>,
    val allResultsByPuzzle: List<Solve>
)
