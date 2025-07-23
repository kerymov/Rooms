package com.kerymov.data_rooms.di

import com.kerymov.data_rooms.dataSources.RemoteRoomsDataSource
import com.kerymov.data_rooms.repository.RoomsRepositoryImpl
import com.kerymov.data_rooms.service.RoomsApi
import com.kerymov.domain_rooms.repository.RoomsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomsModule {

    @Provides
    @Singleton
    fun provideRoomsRepository(remoteRoomsDataSource: RemoteRoomsDataSource): RoomsRepository {
        return RoomsRepositoryImpl(remoteDataSource = remoteRoomsDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteRoomsDataSource(roomsApi: RoomsApi): RemoteRoomsDataSource {
        return RemoteRoomsDataSource(api = roomsApi)
    }

    @Provides
    @Singleton
    fun provideRoomsApi(retrofit: Retrofit): RoomsApi {
        return retrofit.create(RoomsApi::class.java)
    }
}