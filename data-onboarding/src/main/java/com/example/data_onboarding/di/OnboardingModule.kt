package com.example.data_onboarding.di

import com.example.data_onboarding.service.AccountApi
import com.example.data_onboarding.repository.AccountRepositoryImpl
import com.example.data_onboarding.dataSources.RemoteAccountDataSource
import com.example.data_onboarding.utils.UserMapper
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
    fun provideAccountApi(retrofit: Retrofit): AccountApi {
        return retrofit.create(AccountApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAccountRepository(
        remoteDataSource: RemoteAccountDataSource,
        mapper: UserMapper
    ): AccountRepository {
        return AccountRepositoryImpl(remoteDataSource, mapper)
    }
}