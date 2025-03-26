package com.example.data_core.preferences

import com.example.data_core.utils.Crypto
import com.example.data_core.dataStore.DataStorePreferences
import com.example.domain_core.preferences.SecurePreferences

class SecurePreferencesImpl(
    private val dataStorePreferences: DataStorePreferences,
    private val crypto: Crypto
) : SecurePreferences {

    override suspend fun saveAuthToken(token: String, expiresIn: Int) {
        val encryptedToken = crypto.encrypt(token.encodeToByteArray())
        val encryptedExpiresIn = crypto.encrypt(expiresIn.toString().encodeToByteArray())

        dataStorePreferences.putByteArray(
            key = Key.AUTH_TOKEN.name,
            value = encryptedToken
        )
        dataStorePreferences.putByteArray(
            key = Key.AUTH_TOKEN_EXPIRES_IN.name,
            value = encryptedExpiresIn
        )
    }

    override suspend fun getAuthToken(): String? {
        val byteArray = dataStorePreferences.getByteArray(Key.AUTH_TOKEN.name)
        return byteArray?.decodeToString()
    }

    override suspend fun getExpiresIn(): Int? {
        val byteArray = dataStorePreferences.getByteArray(Key.AUTH_TOKEN_EXPIRES_IN.name)
        return byteArray?.decodeToString()?.toIntOrNull()
    }

    override suspend fun clear() {
        dataStorePreferences.removeByteArray(Key.AUTH_TOKEN.name)
        dataStorePreferences.removeByteArray(Key.AUTH_TOKEN_EXPIRES_IN.name)
    }

    private enum class Key {
        AUTH_TOKEN,
        AUTH_TOKEN_EXPIRES_IN
    }
}