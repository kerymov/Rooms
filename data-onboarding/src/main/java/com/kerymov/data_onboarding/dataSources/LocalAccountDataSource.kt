package com.kerymov.data_onboarding.dataSources

import com.kerymov.domain_core.model.User
import com.kerymov.domain_core.preferences.Preferences

class LocalAccountDataSource(
    private val preferences: Preferences,
) {
    suspend fun saveUser(username: String, authToken: String, expiresIn: Int) {
        preferences.saveUser(User(username, authToken, expiresIn))
    }
}