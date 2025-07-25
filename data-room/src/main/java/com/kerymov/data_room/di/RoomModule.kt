package com.kerymov.data_room.di

import com.kerymov.data_room.dataSources.RemoteRoomDataSource
import com.kerymov.data_room.repository.RoomRepositoryImpl
import com.kerymov.data_room.services.RoomService
import com.kerymov.data_room.services.ScrambleApi
import com.kerymov.domain_core.auth.AuthTokenProvider
import com.kerymov.domain_room.repository.RoomRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideRoomRepository(remoteRoomsDataSource: RemoteRoomDataSource): RoomRepository {
        return RoomRepositoryImpl(remoteDataSource = remoteRoomsDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteRoomsDataSource(roomsService: RoomService, scrambleApi: ScrambleApi): RemoteRoomDataSource {
        return RemoteRoomDataSource(service = roomsService, scrambleApi = scrambleApi)
    }

    @Provides
    fun provideRoomService(authTokenProvider: AuthTokenProvider): RoomService {
        return RoomService(authTokenProvider)
    }

    @Provides
    @Singleton
    fun provideScrambleApi(retrofit: Retrofit): ScrambleApi {
        return retrofit.create(ScrambleApi::class.java)
    }
}