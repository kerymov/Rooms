package com.example.data_rooms.dataSources

import com.example.data_rooms.models.requests.CreateRoomRequest
import com.example.data_rooms.models.responses.CreateRoomResponse
import com.example.data_rooms.models.requests.LoginRoomRequest
import com.example.data_rooms.models.responses.LoginRoomResponse
import com.example.data_rooms.models.RoomDto
import com.example.data_rooms.service.RoomsApi
import com.example.network_core.utils.NetworkResult
import com.example.network_core.utils.handleApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RemoteRoomsDataSource(
    private val api: RoomsApi
) {

    val allRooms: Flow<NetworkResult<List<RoomDto>>> = flow {
        emit(handleApi { api.getRooms() })
    }

    suspend fun createRoom(createRoomRequest: CreateRoomRequest): NetworkResult<CreateRoomResponse> {
        return handleApi { api.createRoom(createRoomRequest) }
    }

    suspend fun loginRoom(loginRoomRequest: LoginRoomRequest): NetworkResult<LoginRoomResponse> {
        return handleApi { api.loginRoom(loginRoomRequest) }
    }

    suspend fun deleteRoom(id: String): NetworkResult<Boolean> {
        return handleApi { api.deleteRoom(id) }
    }
}