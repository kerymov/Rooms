package com.kerymov.data_room.services

import com.kerymov.data_common_speedcubing.models.ScrambleDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ScrambleApi {

    @GET("Scramble")
    suspend fun getScramble(@Query("puzzle") puzzle: Int): Response<ScrambleDto>
}