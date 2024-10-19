package com.example.rooms.data.utils

import com.example.rooms.data.model.account.auth.UserDto
import com.example.rooms.domain.model.User

class SessionManager(
    private val dataStorePreferences: DataStorePreferences,
) {
    suspend fun getUser(): User? {
        val userJson = dataStorePreferences.getString(key = USER_KEY)
        val userDto = userJson?.let { UserDto.fromJson(it) }

        return userDto?.toUser()
    }

    suspend fun saveUser(user: User) {
        val user = UserDto(user.username, user.token, user.expiresIn)

        dataStorePreferences.putString(
            key = USER_KEY,
            value = user.toJson()
        )
    }

    private companion object {
        const val USER_KEY = "user"
    }
}

private fun UserDto.toUser(): User {
    return with(this) {
        User(
            username = username,
            token =  token,
            expiresIn = expiresIn,
        )
    }
}