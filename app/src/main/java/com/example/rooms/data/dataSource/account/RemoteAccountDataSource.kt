package com.example.rooms.data.dataSource.account

import com.example.rooms.data.model.account.auth.network.UserAuthResponse
import com.example.rooms.data.model.account.auth.network.UserSignInRequest
import com.example.rooms.data.model.account.auth.network.UserSignUpRequest
import com.example.rooms.data.network.RetrofitInstance
import com.example.rooms.data.network.account.AccountApi
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