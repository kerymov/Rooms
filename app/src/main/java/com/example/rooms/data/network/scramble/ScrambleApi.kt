package com.example.rooms.data.network.scramble

import com.example.rooms.data.model.rooms.ScrambleDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ScrambleApi {

    @GET("Scramble")
    suspend fun getScramble(@Query("puzzle") puzzle: Int): Response<ScrambleDto>
}