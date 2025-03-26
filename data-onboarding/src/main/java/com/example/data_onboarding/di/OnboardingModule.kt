package com.example.data_onboarding.di

import com.example.data_onboarding.dataSources.LocalAccountDataSource
import com.example.data_onboarding.service.AccountApi
import com.example.data_onboarding.repository.AccountRepositoryImpl
import com.example.data_onboarding.dataSources.RemoteAccountDataSource
import com.example.data_onboarding.utils.UserMapper
import com.example.domain_core.preferences.Preferences
import com.example.domain_core.preferences.SecurePreferences
import com.example.domain_onboarding.repository.AccountRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OnboardingModule {

    @Provides
    @Singleton
    fun provideAccountRepository(
        localDataSource: LocalAccountDataSource,
        remoteDataSource: RemoteAccountDataSource,
        mapper: UserMapper
    ): AccountRepository {
        return AccountRepositoryImpl(
            remoteDataSource = remoteDataSource,
            localAccountDataSource = localDataSource,
            mapper = mapper
        )
    }

    @Provides
    @Singleton
    fun provideLocalAccountDataSource(
        preferences: Preferences,
        securePreferences: SecurePreferences
    ): LocalAccountDataSource {
        return LocalAccountDataSource(
            preferences = preferences,
            securePreferences = securePreferences
        )
    }

    @Provides
    @Singleton
    fun provideRemoteAccountDataSource(
        accountApi: AccountApi
    ): RemoteAccountDataSource {
        return RemoteAccountDataSource(api = accountApi)
    }

    @Provides
    @Singleton
    fun provideAccountApi(retrofit: Retrofit): AccountApi {
        return retrofit.create(AccountApi::class.java)
    }
}