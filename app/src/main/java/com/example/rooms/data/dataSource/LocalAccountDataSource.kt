package com.example.rooms.data.dataSource

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.rooms.data.model.account.auth.UserDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "Preferences")

class LocalAccountDataSource(context: Context) {

    private companion object {
        val USER_KEY = stringPreferencesKey("user")
    }

    private val dataStore = context.dataStore

    suspend fun saveUser(user: UserDto) {
        dataStore.edit { preferences ->
            preferences[USER_KEY] = user.toJson()
        }
    }

    fun getUser(): Flow<UserDto?> {
        return dataStore.data
            .map { preferences ->
                val userJson = preferences[USER_KEY]
                userJson?.let { UserDto.fromJson(it) }
            }
    }

    suspend fun removeUser() {
        dataStore.edit { preferences ->
            preferences.remove(USER_KEY)
        }
    }
}