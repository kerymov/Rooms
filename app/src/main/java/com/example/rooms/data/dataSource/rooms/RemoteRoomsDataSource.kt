package com.example.rooms.data.dataSource.rooms

import com.example.rooms.data.model.rooms.CreateRoomRequest
import com.example.rooms.data.model.rooms.RoomDetailsDto
import com.example.rooms.data.model.rooms.RoomDto
import com.example.rooms.data.network.NetworkResult
import com.example.rooms.data.network.RetrofitInstance
import com.example.rooms.data.network.handleApi
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class RemoteRoomsDataSource(token: String?) {

    suspend fun getRooms(): NetworkResult<List<RoomDto>> {
        return handleApi { roomsApi.getRooms() }
    }

    suspend fun createRoom(createRoomRequest: CreateRoomRequest): NetworkResult<RoomDetailsDto> {
        return handleApi { roomsApi.createRoom(createRoomRequest) }
    }

    private val roomsApi = RetrofitInstance.provideRoomsApi(token)
    private val scrambleApi = RetrofitInstance.provideScrambleApi(token)
}