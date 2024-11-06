package com.example.rooms.common

import android.app.Application
import com.example.rooms.data.dataSource.account.RemoteAccountDataSource
import com.example.rooms.data.dataSource.rooms.RemoteRoomsDataSource
import com.example.rooms.data.network.RetrofitInstance
import com.example.rooms.data.repository.AccountRepositoryImpl
import com.example.rooms.data.repository.RoomsRepositoryImpl
import com.example.rooms.data.utils.AppSharedPreferences
import com.example.rooms.domain.repository.AccountRepository
import com.example.rooms.domain.repository.RoomsRepository

class RoomsApp : Application() {

    lateinit var remoteAccountDataSource: RemoteAccountDataSource
    lateinit var remoteRoomsDataSource: RemoteRoomsDataSource
    lateinit var accountRepository: AccountRepository
    lateinit var roomsRepository: RoomsRepository

    override fun onCreate() {
        super.onCreate()

        AppSharedPreferences.init(context = this)

        val accountApi = RetrofitInstance.provideAccountApi()
        remoteAccountDataSource = RemoteAccountDataSource(accountApi)

        val roomsApi = RetrofitInstance.provideRoomsApi(AppSharedPreferences.authToken)
        remoteRoomsDataSource = RemoteRoomsDataSource(roomsApi)

        accountRepository = AccountRepositoryImpl(remoteAccountDataSource)
        roomsRepository = RoomsRepositoryImpl(remoteRoomsDataSource)
    }
}