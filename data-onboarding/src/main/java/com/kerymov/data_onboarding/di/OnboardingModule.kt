package com.kerymov.data_onboarding.di

import com.kerymov.data_onboarding.dataSources.LocalAccountDataSource
import com.kerymov.data_onboarding.dataSources.RemoteAccountDataSource
import com.kerymov.data_onboarding.repository.AccountRepositoryImpl
import com.kerymov.data_onboarding.service.AccountApi
import com.kerymov.data_onboarding.mappers.UserMapper
import com.kerymov.domain_core.preferences.Preferences
import com.kerymov.domain_core.utils.coroutines.IoDispatcher
import com.kerymov.domain_onboarding.repository.AccountRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
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
        mapper: UserMapper,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): AccountRepository {
        return AccountRepositoryImpl(
            remoteDataSource = remoteDataSource,
            localAccountDataSource = localDataSource,
            mapper = mapper,
            dispatcher = dispatcher
        )
    }

    @Provides
    @Singleton
    fun provideLocalAccountDataSource(
        preferences: Preferences,
    ): LocalAccountDataSource {
        return LocalAccountDataSource(preferences = preferences)
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