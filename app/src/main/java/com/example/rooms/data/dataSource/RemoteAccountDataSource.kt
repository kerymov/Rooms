package com.example.rooms.data.dataSource

import com.example.rooms.data.model.account.auth.UserAuthResponseDto
import com.example.rooms.data.model.account.auth.UserSignInRequestDto
import com.example.rooms.data.model.account.auth.UserSignUpRequestDto
import com.example.rooms.data.network.NetworkResult
import com.example.rooms.data.network.RetrofitInstance
import com.example.rooms.data.network.handleApi

class RemoteAccountDataSource {
    suspend fun signIn(userSignInRequest: UserSignInRequestDto): NetworkResult<UserAuthResponseDto> {
        return handleApi { RetrofitInstance.accountApi.signIn(userSignInRequest) }
    }

    suspend fun signUp(userSignUpRequest: UserSignUpRequestDto): NetworkResult<UserAuthResponseDto> {
        return handleApi { RetrofitInstance.accountApi.signUp(userSignUpRequest) }
    }
}