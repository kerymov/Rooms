package com.kerymov.data_core.di

import android.content.Context
import com.kerymov.data_core.mappers.UserMapper
import com.kerymov.data_core.preferences.PreferencesImpl
import com.kerymov.data_core.utils.AuthTokenProviderImpl
import com.kerymov.data_core.utils.UserProviderImpl
import com.kerymov.domain_core.auth.AuthTokenProvider
import com.kerymov.domain_core.preferences.Preferences
import com.kerymov.domain_core.user.UserProvider
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
    fun provideAuthTokenProvider(preferences: Preferences): AuthTokenProvider {
        return AuthTokenProviderImpl(preferences)
    }

    @Provides
    @Singleton
    fun provideUserProvider(preferences: Preferences): UserProvider {
        return UserProviderImpl(preferences)
    }

    @Provides
    @Singleton
    fun provideUserPreferences(
        @ApplicationContext context: Context,
        mapper: UserMapper
    ): Preferences {
        return PreferencesImpl(context, mapper)
    }

    @Provides
    @Singleton
    fun provideUserMapper(): UserMapper {
        return UserMapper()
    }
}