package com.example.domain_profile.repository

import com.example.domain_core.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getUser(): Flow<User?>

    suspend fun signOut()
}