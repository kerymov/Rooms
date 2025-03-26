package com.example.data_core.preferences

import com.example.data_core.dataStore.DataStorePreferences
import com.example.domain_core.preferences.Preferences

class PreferencesImpl(
    private val dataStorePreferences: DataStorePreferences
): Preferences {

    override suspend fun getUsername(): String? {
        return dataStorePreferences.getString(Key.USER_NAME.name)
    }

    override suspend fun saveUsername(username: String) {
        dataStorePreferences.putString(Key.USER_NAME.name, username)
    }

    override suspend fun clear() {
        dataStorePreferences.removeString(Key.USER_NAME.name)
    }

    private enum class Key {
        USER_NAME
    }
}
