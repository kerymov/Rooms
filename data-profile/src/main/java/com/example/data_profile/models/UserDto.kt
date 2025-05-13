package com.example.data_profile.models

import com.google.gson.Gson

data class UserDto(
    val username: String,
    val token: String,
    val expiresIn: Int,
) {
    fun toJson(): String = Gson().toJson(this)

    companion object {
        fun fromJson(json: String): UserDto = Gson().fromJson(json, UserDto::class.java)
    }
}
