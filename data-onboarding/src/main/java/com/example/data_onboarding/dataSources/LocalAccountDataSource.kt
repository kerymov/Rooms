package com.example.data_onboarding.dataSources

import com.example.domain_core.model.User
import com.example.domain_core.preferences.Preferences

class LocalAccountDataSource(
    private val preferences: Preferences,
) {
    suspend fun saveUser(username: String, authToken: String, expiresInt: Int) {
        preferences.saveUser(User(username, authToken, expiresInt))
    }
}