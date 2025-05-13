package com.example.data_onboarding.service

import com.example.data_onboarding.models.UserAuthResponse
import com.example.data_onboarding.models.UserSignInRequest
import com.example.data_onboarding.models.UserSignUpRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AccountApi {

    @POST("Account/login")
    suspend fun signIn(@Body user: UserSignInRequest): Response<UserAuthResponse>

    @POST("Account/register")
    suspend fun signUp(@Body user: UserSignUpRequest): Response<UserAuthResponse>
}