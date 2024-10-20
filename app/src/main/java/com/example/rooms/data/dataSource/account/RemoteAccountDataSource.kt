package com.example.rooms.data.dataSource.account

import com.example.rooms.data.model.account.auth.network.UserAuthResponseDto
import com.example.rooms.data.model.account.auth.network.UserSignInRequestDto
import com.example.rooms.data.model.account.auth.network.UserSignUpRequestDto
import com.example.rooms.data.network.RetrofitInstance
import retrofit2.Response

class RemoteAccountDataSource {

    private val accountApi = RetrofitInstance.provideAccountApi()

    suspend fun signIn(userSignInRequest: UserSignInRequestDto): Response<UserAuthResponseDto> {
        return accountApi.signIn(userSignInRequest)
    }

    suspend fun signUp(userSignUpRequest: UserSignUpRequestDto): Response<UserAuthResponseDto> {
        return accountApi.signUp(userSignUpRequest)
    }
}