package com.example.domain_rooms.repository

import com.example.domain_core.utils.BaseResult
import com.example.domain_rooms.models.Room
import com.example.domain_rooms.models.RoomDetails
import com.example.domain_rooms.models.RoomSettings
import kotlinx.coroutines.flow.Flow

interface RoomsRepository {

    val allRooms: Flow<BaseResult<List<Room>>>

    suspend fun createRoom(
        name: String,
        password: String?,
        settings: RoomSettings
    ): BaseResult<RoomDetails>

    suspend fun loginRoom(
        name: String,
        password: String?
    ): BaseResult<RoomDetails>

    suspend fun deleteRoom(id: String): BaseResult<Boolean>
}