package com.example.rooms.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class Solve(
    val username: String,
    val roomName: String,
    val resultInMills: Long
)

object SolvesRepository {

    private var solves: List<Solve> by mutableStateOf(listOf())

    fun addSolve(solve: Solve) {
        solves += solve
    }

    fun getBestResult(username: String, roomName: String) = solves
        .filter { it.username == username }
        .filter { it.roomName == roomName }
        .minBy { it.resultInMills }
        .resultInMills

    fun getLastAverageOf(n: Int, username: String, roomName: String): Long? {
        val userSolvesInRoom = solves
            .filter { it.username == username }
            .filter { it.roomName == roomName }

        if (userSolvesInRoom.size < n) return null

        val lastNSolves = userSolvesInRoom.take(n)
        return lastNSolves.calculateAverage()
    }

    fun getMeanOfAllSolves(username: String, roomName: String): Long {
        val userSolvesInRoom = solves
            .filter { it.username == username }
            .filter { it.roomName == roomName }

        return userSolvesInRoom.sumOf { it.resultInMills } / userSolvesInRoom.size
    }

    fun getUserSolvesFromRoom(roomName: String) = solves
        .filter { it.roomName == roomName }
        .groupBy { it.username }
}

private fun List<Solve>.calculateAverage(): Long {
    return this
        .sortedBy { it.resultInMills }
        .drop(1)
        .dropLast(1)
        .sumOf { it.resultInMills } / (this.size - 2)
}