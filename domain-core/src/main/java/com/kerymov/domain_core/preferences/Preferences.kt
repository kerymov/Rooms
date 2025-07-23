package com.kerymov.domain_core.preferences

import com.kerymov.domain_core.model.User
import kotlinx.coroutines.flow.Flow

interface Preferences {

    val user: Flow<User?>

    suspend fun saveUser(user: User)

    suspend fun clear()
}