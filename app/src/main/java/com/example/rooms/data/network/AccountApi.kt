package com.example.rooms.data.network

import com.example.rooms.data.models.UserLoginData
import com.example.rooms.data.models.UserLoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AccountApi {

    @POST("/Account/login")
    suspend fun signIn(@Body user: UserLoginData): UserLoginResponse
}