package com.example.data_core.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.data_core.models.PreferencesDto
import com.example.data_core.utils.Crypto
import com.example.data_core.mappers.UserMapper
import com.example.domain_core.model.User
import com.example.domain_core.preferences.Preferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val PREFERENCES_NAME = "user-preferences"

private val Context.dataStore: DataStore<PreferencesDto> by dataStore(
    fileName = PREFERENCES_NAME,
    serializer = PreferencesSerializer(PreferencesDto(), Crypto)
)

class PreferencesImpl(
    context: Context,
    private val mapper: UserMapper
): Preferences {

    private val dataStore = context.dataStore

    override val user: Flow<User?>
        get() = dataStore.data.map { preferences ->
            preferences.user?.let { mapper.mapToDomain(it) }
        }

    override suspend fun saveUser(user: User) {
        dataStore.updateData { preferences ->
            preferences.copy(user = mapper.mapFromDomain(user))
        }
    }

    override suspend fun clear() {
        dataStore.updateData { PreferencesDto() }
    }

}
