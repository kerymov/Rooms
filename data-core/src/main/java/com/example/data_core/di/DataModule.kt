package com.example.data_core.di

import android.content.Context
import com.example.data_core.preferences.PreferencesImpl
import com.example.data_core.preferences.SecurePreferencesImpl
import com.example.data_core.utils.Crypto
import com.example.data_core.dataStore.DataStorePreferences
import com.example.domain_core.preferences.Preferences
import com.example.domain_core.preferences.SecurePreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun providePreferences(dataStorePreferences: DataStorePreferences): Preferences {
        return PreferencesImpl(dataStorePreferences = dataStorePreferences)
    }

    @Provides
    @Singleton
    fun provideSecurePreferences(dataStorePreferences: DataStorePreferences, crypto: Crypto): SecurePreferences {
        return SecurePreferencesImpl(dataStorePreferences = dataStorePreferences, crypto = crypto)
    }

    @Provides
    @Singleton
    fun provideDataStorePreferences(@ApplicationContext context: Context): DataStorePreferences {
        return DataStorePreferences(context = context)
    }

    @Provides
    @Singleton
    fun provideCrypto(): Crypto {
        return Crypto
    }
}