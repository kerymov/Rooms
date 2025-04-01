package com.example.domain_core.preferences

import com.example.domain_core.model.User
import kotlinx.coroutines.flow.Flow

interface Preferences {

    val user: Flow<User?>

    suspend fun saveUser(user: User)

    suspend fun clear()
}