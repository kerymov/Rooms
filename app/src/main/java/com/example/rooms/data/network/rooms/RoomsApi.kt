package com.example.rooms.data.network.rooms

import com.example.rooms.data.model.rooms.RoomDto
import com.example.rooms.data.model.rooms.requests.CreateRoomRequest
import com.example.rooms.data.model.rooms.responses.CreateRoomResponse
import com.example.rooms.data.model.rooms.requests.LoginRoomRequest
import com.example.rooms.data.model.rooms.responses.LoginRoomResponse
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
    suspend fun createRoom(@Body createRoomRequest: CreateRoomRequest): Response<CreateRoomResponse>

    @POST("rooms/login")
    suspend fun loginRoom(@Body loginRoomRequest: LoginRoomRequest): Response<LoginRoomResponse>

    @DELETE("rooms/{roomId}")
    suspend fun deleteRoom(@Path("roomId") id: String)
}