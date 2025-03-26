package com.example.data_onboarding.dataSources

import com.example.domain_core.preferences.Preferences
import com.example.domain_core.preferences.SecurePreferences

class LocalAccountDataSource(
    private val preferences: Preferences,
    private val securePreferences: SecurePreferences,
) {
    suspend fun saveUser(username: String, authToken: String, expiresInt: Int) {
        preferences.saveUsername(username)
        securePreferences.saveAuthToken(authToken, expiresInt)
    }
}