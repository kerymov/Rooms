package com.example.rooms.data.remote.account

import com.example.rooms.data.remote.account.models.UserLoginRequest
import com.example.rooms.data.remote.account.models.UserLoginResponse
import com.example.rooms.data.remote.account.models.UserResults
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AccountApi {

    @POST("Account/login")
    suspend fun signIn(@Body user: UserLoginRequest): Response<UserLoginResponse>

    @GET("Account/results")
    suspend fun getResults(): Response<UserResults>
}