package com.example.rooms.data.dataSource.rooms

import com.example.rooms.data.model.rooms.RoomDto
import com.example.rooms.data.network.RetrofitInstance
import retrofit2.Response

class RemoteRoomsDataSource(token: String?) {

    private val roomsApi = RetrofitInstance.provideRoomsApi(token)
    private val scrambleApi = RetrofitInstance.provideScrambleApi(token)

    suspend fun getRooms(): Response<List<RoomDto>> {
        return roomsApi.getRooms()
    }
}