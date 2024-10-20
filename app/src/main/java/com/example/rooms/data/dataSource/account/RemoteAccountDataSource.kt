package com.example.rooms.data.dataSource.account

import com.example.rooms.data.model.account.auth.network.UserAuthResponse
import com.example.rooms.data.model.account.auth.network.UserSignInRequest
import com.example.rooms.data.model.account.auth.network.UserSignUpRequest
import com.example.rooms.data.network.RetrofitInstance
import retrofit2.Response

class RemoteAccountDataSource {

    private val accountApi = RetrofitInstance.provideAccountApi()

    suspend fun signIn(userSignInRequest: UserSignInRequest): Response<UserAuthResponse> {
        return accountApi.signIn(userSignInRequest)
    }

    suspend fun signUp(userSignUpRequest: UserSignUpRequest): Response<UserAuthResponse> {
        return accountApi.signUp(userSignUpRequest)
    }
}