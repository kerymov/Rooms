package com.example.rooms.data.dataSource.rooms

import com.example.rooms.data.model.rooms.CreateRoomRequest
import com.example.rooms.data.model.rooms.CreateRoomResponse
import com.example.rooms.data.model.rooms.RoomDetailsDto
import com.example.rooms.data.model.rooms.RoomDto
import com.example.rooms.data.network.NetworkResult
import com.example.rooms.data.network.RetrofitInstance
import com.example.rooms.data.network.handleApi
import com.example.rooms.data.network.rooms.RoomsApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RemoteRoomsDataSource(
    private val api: RoomsApi
) {

    val allRooms: Flow<NetworkResult<List<RoomDto>>> = flow {
        emit(handleApi { api.getRooms() })
    }

    suspend fun createRoom(createRoomRequest: CreateRoomRequest): NetworkResult<CreateRoomResponse> {
        return handleApi { api.createRoom(createRoomRequest) }
    }
}