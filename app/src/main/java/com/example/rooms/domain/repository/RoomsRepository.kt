package com.example.rooms.domain.repository

import com.example.rooms.domain.model.BaseResult
import com.example.rooms.domain.model.Room
import kotlinx.coroutines.flow.Flow

interface RoomsRepository {

    suspend fun getRooms(): Flow<BaseResult<List<Room>>>

    suspend fun createRoom()
}