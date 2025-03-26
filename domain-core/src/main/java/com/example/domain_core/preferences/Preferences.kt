package com.example.domain_core.preferences

interface Preferences {

    suspend fun getUsername(): String?

    suspend fun saveUsername(username: String)

    suspend fun clear()
}