package com.example.rooms.data.network

import com.example.rooms.data.models.Room
import com.example.rooms.data.models.RoomsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RoomsApi {

    @GET("/rooms")
    suspend fun getRooms(): List<Room>

    @POST("/rooms")
    suspend fun createRoom(@Body room: Room): Room
}