package com.kerymov.data_room.dataSources

import com.kerymov.data_common_speedcubing.models.ResultDto
import com.kerymov.data_common_speedcubing.models.ScrambleDto
import com.kerymov.data_common_speedcubing.models.SolveDto
import com.kerymov.data_room.models.NewSolveResultDto
import com.kerymov.data_room.services.RoomService
import com.kerymov.data_room.services.ScrambleApi
import com.kerymov.network_core.utils.NetworkResult
import com.kerymov.network_core.utils.handleApi
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