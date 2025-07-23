package com.kerymov.domain_room.repository

import com.kerymov.domain_common_speedcubing.models.Scramble
import com.kerymov.domain_common_speedcubing.models.Result
import com.kerymov.domain_common_speedcubing.models.Solve
import com.kerymov.domain_core.utils.BaseResult
import com.kerymov.domain_room.models.NewSolveResult
import com.kerymov.domain_room.models.User
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