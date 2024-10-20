package com.example.rooms.data.network.rooms

import com.example.rooms.data.model.rooms.RoomDto
import com.example.rooms.data.model.rooms.RoomCreationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RoomsApi {

    @GET("rooms")
    suspend fun getRooms(): Response<List<RoomDto>>

    @POST("rooms")
    suspend fun createRoom(@Body roomCreationRequest: RoomCreationRequest)

    @DELETE("rooms/{roomId}")
    suspend fun deleteRoom(@Path("roomId") id: String)
}