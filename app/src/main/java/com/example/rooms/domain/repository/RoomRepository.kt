package com.example.rooms.domain.repository

import com.example.domain_rooms.models.Result
import com.example.domain_rooms.models.NewSolveResult
import com.example.domain_rooms.models.Scramble
import com.example.domain_rooms.models.Solve
import com.example.domain_rooms.models.User
import com.example.rooms.domain.model.BaseResult
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