package com.example.data_core.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.byteArrayPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject

private const val PREFERENCES_NAME = "Preferences"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

class DataStorePreferences @Inject constructor(context: Context) {

    private val dataStore = context.dataStore

    suspend fun putString(key: String, value: String) {
        val preferencesKey = stringPreferencesKey(key)
        dataStore.edit { preferences ->
            preferences[preferencesKey] = value
        }
    }

    suspend fun putInt(key: String, value: Int) {
        val preferencesKey = intPreferencesKey(key)
        dataStore.edit { preferences ->
            preferences[preferencesKey] = value
        }
    }

    suspend fun putByteArray(key: String, value: ByteArray) {
        val preferencesKey = byteArrayPreferencesKey(key)
        dataStore.edit { preferences ->
            preferences[preferencesKey] = value
        }
    }

    suspend fun getString(key: String): String? {
        val preferencesKey = stringPreferencesKey(key)
        val preferences = dataStore.data.first()
        return preferences[preferencesKey]
    }

    suspend fun getInt(key: String): Int? {
        val preferencesKey = intPreferencesKey(key)
        val preferences = dataStore.data.first()
        return preferences[preferencesKey]
    }

    suspend fun getByteArray(key: String): ByteArray? {
        val preferencesKey = byteArrayPreferencesKey(key)
        val preferences = dataStore.data.first()
        return preferences[preferencesKey]
    }

    suspend fun removeString(key: String) {
        val preferencesKey = stringPreferencesKey(key)
        dataStore.edit { preferences ->
            preferences.remove(preferencesKey)
        }
    }

    suspend fun removeInt(key: String) {
        val preferencesKey = intPreferencesKey(key)
        dataStore.edit { preferences ->
            preferences.remove(preferencesKey)
        }
    }

    suspend fun removeByteArray(key: String) {
        val preferencesKey = byteArrayPreferencesKey(key)
        dataStore.edit { preferences ->
            preferences.remove(preferencesKey)
        }
    }
}