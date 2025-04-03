package com.example.rooms.data.dataSource.room

import com.example.data_rooms.models.NewSolveResultDto
import com.example.data_rooms.models.ResultDto
import com.example.data_rooms.models.ScrambleDto
import com.example.data_rooms.models.SolveDto
import com.example.rooms.data.network.NetworkResult
import com.example.rooms.data.network.handleApi
import com.example.rooms.data.network.room.RoomService
import com.example.rooms.data.network.scramble.ScrambleApi
import kotlinx.coroutines.flow.Flow

class RemoteRoomDataSource(
    private val service: RoomService,
    private val scrambleApi: ScrambleApi
) {

    fun joinRoom(roomName: String, onComplete: () -> Unit) = service.joinRoom(roomName, onComplete)

    fun leaveRoom(roomName: String) = service.leaveRoom(roomName)

    fun sendSolveResult(result: NewSolveResultDto) = service.sendSolveResult(result)

    fun askForNewSolve(roomId: String) = service.askForNewSolve(roomId)

    val newUsers: Flow<String> = service.newUsers

    val leftUsers: Flow<String> = service.leftUsers

    val finishedSolves: Flow<SolveDto> = service.finishedSolves

    val results: Flow<ResultDto> = service.results

    suspend fun getScramble(puzzle: Int): NetworkResult<ScrambleDto> {
        return handleApi { scrambleApi.getScramble(puzzle) }
    }
}