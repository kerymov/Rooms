package com.example.domain_core.preferences

interface SecurePreferences {

    suspend fun saveAuthToken(token: String, expiresIn: Int)

    suspend fun getAuthToken(): String?

    suspend fun getExpiresIn(): Int?

    suspend fun clear()
}