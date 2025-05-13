package com.example.data_profile.dataSources

import com.example.domain_core.model.User
import com.example.domain_core.preferences.Preferences
import kotlinx.coroutines.flow.Flow

class LocalUserDataSource (
    private val preferences: Preferences
) {
    fun getUser(): Flow<User?> = preferences.user

    suspend fun signOut() = preferences.clear()
}