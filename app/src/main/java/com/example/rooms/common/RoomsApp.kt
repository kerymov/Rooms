package com.example.rooms.common

import android.app.Application
import com.example.data_room.dataSources.RemoteRoomDataSource
import com.example.data_room.repository.RoomRepositoryImpl
import com.example.data_room.services.RoomService
import com.example.domain_room.repository.RoomRepository
import com.example.rooms.data.network.RetrofitInstance
import com.example.rooms.data.utils.AppSharedPreferences
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RoomsApp : Application() {

    lateinit var remoteRoomDataSource: RemoteRoomDataSource
    lateinit var roomRepository: RoomRepository

    override fun onCreate() {
        super.onCreate()

        AppSharedPreferences.init(context = this)

        val authToken = AppSharedPreferences.authToken

        val roomService = RoomService(authToken)
        val scrambleApi = RetrofitInstance.provideScrambleApi(authToken)
        remoteRoomDataSource = RemoteRoomDataSource(roomService, scrambleApi)

        roomRepository = RoomRepositoryImpl(remoteRoomDataSource)
    }
}