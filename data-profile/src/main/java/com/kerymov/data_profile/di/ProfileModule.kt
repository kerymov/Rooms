package com.kerymov.data_profile.di

import com.kerymov.data_profile.dataSources.LocalUserDataSource
import com.kerymov.data_profile.dataSources.RemoteUserDataSource
import com.kerymov.data_profile.repository.UserRepositoryImpl
import com.kerymov.data_profile.service.UserApi
import com.kerymov.domain_core.preferences.Preferences
import com.kerymov.domain_profile.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileModule {

    @Provides
    @Singleton
    fun provideUserRepository(
        localDataSource: LocalUserDataSource,
        remoteDataSource: RemoteUserDataSource,
    ): UserRepository {
        return UserRepositoryImpl(
            remoteUserDataSource = remoteDataSource,
            localUserDataSource = localDataSource
        )
    }

    @Provides
    @Singleton
    fun provideLocalUserDataSource(
        preferences: Preferences
    ): LocalUserDataSource {
        return LocalUserDataSource(preferences = preferences)
    }

    @Provides
    @Singleton
    fun provideRemoteUserDataSource(
        userApi: UserApi
    ): RemoteUserDataSource {
        return RemoteUserDataSource(userApi = userApi)
    }

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }
}