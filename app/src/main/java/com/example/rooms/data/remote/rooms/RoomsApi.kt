package com.example.rooms.data.remote.rooms

import com.example.rooms.data.remote.rooms.models.Room
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RoomsApi {

    @GET("rooms")
    suspend fun getRooms(): Response<List<Room>>

    @POST("rooms")
    suspend fun createRoom(@Body room: Room): Room
}