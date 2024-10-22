package com.example.rooms.data.dataSource.rooms

import com.example.rooms.data.model.rooms.RoomDto
import com.example.rooms.data.network.NetworkResult
import com.example.rooms.data.network.RetrofitInstance
import com.example.rooms.data.network.handleApi
import retrofit2.Response

class RemoteRoomsDataSource(token: String?) {

    private val roomsApi = RetrofitInstance.provideRoomsApi(token)
    private val scrambleApi = RetrofitInstance.provideScrambleApi(token)

    suspend fun getRooms(): NetworkResult<List<RoomDto>> {
        return handleApi { roomsApi.getRooms() }
    }
}