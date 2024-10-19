package com.example.rooms.domain.repository

import com.example.rooms.domain.model.BaseResult
import com.example.rooms.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

    suspend fun signIn(username: String, password: String): Flow<BaseResult<User>>

    suspend fun signUp(username: String, password: String, passwordConfirm: String): Flow<BaseResult<User>>

    suspend fun signOut()

    suspend fun saveUser(user: User)

    suspend fun getUser(): User?
}