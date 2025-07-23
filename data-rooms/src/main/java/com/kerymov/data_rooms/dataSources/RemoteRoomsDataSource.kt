package com.kerymov.data_rooms.dataSources

import com.kerymov.data_rooms.models.requests.CreateRoomRequest
import com.kerymov.data_rooms.models.responses.CreateRoomResponse
import com.kerymov.data_rooms.models.requests.LoginRoomRequest
import com.kerymov.data_rooms.models.responses.LoginRoomResponse
import com.kerymov.data_rooms.models.RoomDto
import com.kerymov.data_rooms.service.RoomsApi
import com.kerymov.network_core.utils.NetworkResult
import com.kerymov.network_core.utils.handleApi
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

    suspend fun loginRoom(loginRoomRequest: LoginRoomRequest): NetworkResult<LoginRoomResponse> {
        return handleApi { api.loginRoom(loginRoomRequest) }
    }

    suspend fun deleteRoom(id: String): NetworkResult<Boolean> {
        return handleApi { api.deleteRoom(id) }
    }
}