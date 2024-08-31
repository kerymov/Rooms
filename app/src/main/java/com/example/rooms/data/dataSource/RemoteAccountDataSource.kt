package com.example.rooms.data.dataSource

import com.example.rooms.data.model.account.auth.UserAuthResponseDto
import com.example.rooms.data.model.account.auth.UserSignInRequestDto
import com.example.rooms.data.model.account.auth.UserSignUpRequestDto
import com.example.rooms.data.network.NetworkResult
import com.example.rooms.data.network.RetrofitInstance
import com.example.rooms.data.network.handleApi
import retrofit2.Response

class RemoteAccountDataSource {

    suspend fun signIn(userSignInRequest: UserSignInRequestDto): Response<UserAuthResponseDto> {
        return RetrofitInstance.accountApi.signIn(userSignInRequest)
    }

    suspend fun signUp(userSignUpRequest: UserSignUpRequestDto): Response<UserAuthResponseDto> {
        return RetrofitInstance.accountApi.signUp(userSignUpRequest)
    }
}