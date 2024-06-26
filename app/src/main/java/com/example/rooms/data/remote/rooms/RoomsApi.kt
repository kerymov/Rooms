package com.example.rooms.data.remote.rooms

import com.example.rooms.data.remote.rooms.models.Room
import com.example.rooms.data.remote.rooms.models.RoomCreationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RoomsApi {

    @GET("rooms")
    suspend fun getRooms(): Response<List<Room>>

    @POST("rooms")
    suspend fun createRoom(@Body roomCreationRequest: RoomCreationRequest)

    @DELETE("rooms/{roomId}")
    suspend fun deleteRoom(@Path("roomId") id: String)
}