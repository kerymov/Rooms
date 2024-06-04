package com.example.rooms.data.remote.account.models

data class Solve(
    val roomName: String,
    val solveNumber: Int,
    val scramble: String,
    val puzzle: Int,
    val dateAdded: String,
    val time: Int,
    val penalty: Int
)