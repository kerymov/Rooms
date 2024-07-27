package com.example.rooms.data.remote.account

import com.example.rooms.data.remote.account.models.UserSignInRequest
import com.example.rooms.data.remote.account.models.UserAuthResponse
import com.example.rooms.data.remote.account.models.UserSignUpRequest
import com.example.rooms.data.remote.account.models.UserResults
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AccountApi {

    @POST("Account/login")
    suspend fun signIn(@Body user: UserSignInRequest): Response<UserAuthResponse>

    @POST("Account/register")
    suspend fun signUp(@Body user: UserSignUpRequest): Response<UserAuthResponse>

    @GET("Account/results")
    suspend fun getResults(): Response<UserResults>
}