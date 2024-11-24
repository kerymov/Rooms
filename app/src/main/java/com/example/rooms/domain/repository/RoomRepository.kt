package com.example.rooms.domain.repository

import com.example.rooms.domain.model.BaseResult
import com.example.rooms.domain.model.rooms.NewSolveResult
import com.example.rooms.domain.model.rooms.Result
import com.example.rooms.domain.model.rooms.Scramble
import com.example.rooms.domain.model.rooms.Solve
import com.example.rooms.domain.model.rooms.User
import kotlinx.coroutines.flow.Flow

interface RoomRepository {

    fun joinRoom(roomName: String, onComplete: () -> Unit)

    fun leaveRoom(roomName: String)

    fun askForNewSolve(roomId: String)

    fun sendSolveResult(result: NewSolveResult)

    val newUsers: Flow<User>

    val leftUsers: Flow<User>

    val finishedSolves: Flow<Solve>

    val newSolves: Flow<Result>

    suspend fun getScramble(puzzle: Int): BaseResult<Scramble>
}