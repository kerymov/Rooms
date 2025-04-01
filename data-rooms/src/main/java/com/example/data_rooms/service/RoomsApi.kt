package com.example.data_rooms.service

import com.example.data_rooms.models.RoomDto
import com.example.data_rooms.models.requests.CreateRoomRequest
import com.example.data_rooms.models.requests.LoginRoomRequest
import com.example.data_rooms.models.responses.CreateRoomResponse
import com.example.data_rooms.models.responses.LoginRoomResponse
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
    suspend fun deleteRoom(@Path("roomId") id: String): Response<Boolean>
}