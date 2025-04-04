package com.example.rooms.common

import android.app.Application
import com.example.rooms.data.dataSource.room.RemoteRoomDataSource
import com.example.rooms.data.dataSource.rooms.RemoteRoomsDataSource
import com.example.rooms.data.network.RetrofitInstance
import com.example.rooms.data.network.room.RoomService
import com.example.rooms.data.repository.RoomRepositoryImpl
import com.example.rooms.data.repository.RoomsRepositoryImpl
import com.example.rooms.data.utils.AppSharedPreferences
import com.example.rooms.domain.repository.RoomRepository
import com.example.rooms.domain.repository.RoomsRepository
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RoomsApp : Application() {

    lateinit var remoteRoomsDataSource: RemoteRoomsDataSource
    lateinit var remoteRoomDataSource: RemoteRoomDataSource
    lateinit var roomsRepository: RoomsRepository
    lateinit var roomRepository: RoomRepository

    override fun onCreate() {
        super.onCreate()

        AppSharedPreferences.init(context = this)

        val authToken = AppSharedPreferences.authToken

        val roomsApi = RetrofitInstance.provideRoomsApi(authToken)
        remoteRoomsDataSource = RemoteRoomsDataSource(roomsApi)

        val roomService = RoomService(authToken)
        val scrambleApi = RetrofitInstance.provideScrambleApi(authToken)
        remoteRoomDataSource = RemoteRoomDataSource(roomService, scrambleApi)

        roomsRepository = RoomsRepositoryImpl(remoteRoomsDataSource)
        roomRepository = RoomRepositoryImpl(remoteRoomDataSource)
    }
}