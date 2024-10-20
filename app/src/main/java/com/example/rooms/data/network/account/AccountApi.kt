package com.example.rooms.data.network.account

import com.example.rooms.data.model.account.auth.network.UserSignInRequestDto
import com.example.rooms.data.model.account.auth.network.UserAuthResponseDto
import com.example.rooms.data.model.account.auth.network.UserSignUpRequestDto
import com.example.rooms.data.model.account.results.UserResults
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AccountApi {

    @POST("Account/login")
    suspend fun signIn(@Body user: UserSignInRequestDto): Response<UserAuthResponseDto>

    @POST("Account/register")
    suspend fun signUp(@Body user: UserSignUpRequestDto): Response<UserAuthResponseDto>

    @GET("Account/results")
    suspend fun getResults(): Response<UserResults>
}