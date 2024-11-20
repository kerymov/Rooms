package com.example.rooms.data.dataSource.room

import com.example.rooms.data.model.rooms.ResultDto
import com.example.rooms.data.model.rooms.ScrambleDto
import com.example.rooms.data.network.NetworkResult
import com.example.rooms.data.network.handleApi
import com.example.rooms.data.network.room.RoomService
import com.example.rooms.data.network.scramble.ScrambleApi
import kotlinx.coroutines.flow.Flow

class RemoteRoomDataSource(
    private val service: RoomService,
    private val scrambleApi: ScrambleApi
) {

    fun joinRoom(roomName: String) = service.joinRoom(roomName)

    fun leaveRoom(roomName: String) = service.leaveRoom(roomName)

    val newUsers: Flow<String> = service.newUsers

    val leftUsers: Flow<String> = service.leftUsers

    val results: Flow<ResultDto> = service.results

    suspend fun getScramble(puzzle: Int): NetworkResult<ScrambleDto> {
        return handleApi { scrambleApi.getScramble(puzzle) }
    }
}