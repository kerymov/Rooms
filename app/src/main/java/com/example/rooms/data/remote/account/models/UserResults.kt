package com.example.rooms.data.remote.account.models

data class UserResults(
    val bestResultsByPuzzle: List<BestResult>,
    val allResultsByPuzzle: List<Solve>
)
