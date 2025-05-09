package com.example.domain_room.repository

import com.example.domain_common_speedcubing.models.Scramble
import com.example.domain_common_speedcubing.models.Result
import com.example.domain_common_speedcubing.models.Solve
import com.example.domain_core.utils.BaseResult
import com.example.domain_room.models.NewSolveResult
import com.example.domain_room.models.User
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