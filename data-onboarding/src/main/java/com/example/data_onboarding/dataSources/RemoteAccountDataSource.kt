package com.example.data_onboarding.dataSources

import com.example.data_onboarding.models.UserAuthResponse
import com.example.data_onboarding.models.UserSignInRequest
import com.example.data_onboarding.models.UserSignUpRequest
import com.example.data_onboarding.service.AccountApi
import retrofit2.Response

class RemoteAccountDataSource(
    private val api: AccountApi
) {
    suspend fun signIn(userSignInRequest: UserSignInRequest): Response<UserAuthResponse> {
        return api.signIn(userSignInRequest)
    }

    suspend fun signUp(userSignUpRequest: UserSignUpRequest): Response<UserAuthResponse> {
        return api.signUp(userSignUpRequest)
    }
}