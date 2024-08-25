package com.example.rooms.domain.repository

import com.example.rooms.domain.model.BaseResult
import com.example.rooms.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

    suspend fun signIn(username: String, password: String): Flow<BaseResult<User>>

    suspend fun signUp(username: String, password: String, passwordConfirm: String): Flow<BaseResult<User>>

    suspend fun saveUser(username: String, token: String, expiresIn: Int)

    suspend fun getUser(): Flow<BaseResult<User>>
}