package com.example.rooms.data.network.account

import com.example.rooms.data.model.account.auth.network.UserSignInRequest
import com.example.rooms.data.model.account.auth.network.UserAuthResponse
import com.example.rooms.data.model.account.auth.network.UserSignUpRequest
import com.example.rooms.data.model.account.results.UserResults
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