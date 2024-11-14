package com.example.rooms.data.repository

import com.example.rooms.data.dataSource.rooms.RemoteRoomsDataSource
import com.example.rooms.data.model.rooms.CreateRoomRequest
import com.example.rooms.data.model.rooms.LoginOrCreateRoomResponse
import com.example.rooms.data.model.rooms.LoginRoomRequest
import com.example.rooms.data.model.rooms.RoomDto
import com.example.rooms.data.model.rooms.mappers.mapToDomainModel
import com.example.rooms.data.model.rooms.mappers.mapToDto
import com.example.rooms.data.network.NetworkResult
import com.example.rooms.domain.model.BaseResult
import com.example.rooms.domain.model.rooms.Room
import com.example.rooms.domain.model.rooms.RoomDetails
import com.example.rooms.domain.model.rooms.RoomSettings
import com.example.rooms.domain.repository.RoomsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.collections.map

class RoomsRepositoryImpl(
    private val remoteDataSource: RemoteRoomsDataSource
) : RoomsRepository {

    override val allRooms: Flow<BaseResult<List<Room>>> = remoteDataSource.allRooms
        .map { it.mapToRoom() }

    override suspend fun createRoom(
        name: String,
        password: String?,
        settings: RoomSettings
    ): BaseResult<RoomDetails> {
        val createRoomRequest = CreateRoomRequest(
            roomName = name,
            roomPassword = password,
            settings = settings.mapToDto()
        )

        return remoteDataSource.createRoom(createRoomRequest).mapToRoomDetails()
    }

    override suspend fun loginRoom(
        name: String, password: String?
    ): BaseResult<RoomDetails> {
        val loginRoomRequest = LoginRoomRequest(
            roomName = name,
            roomPassword = password
        )

        return remoteDataSource.loginRoom(loginRoomRequest).mapToRoomDetails()
    }
}

fun NetworkResult<List<RoomDto>>.mapToRoom(): BaseResult<List<Room>> {
    return when (this) {
        is NetworkResult.Success -> {
            val rooms = data.map { roomDto -> roomDto.mapToDomainModel() }

            BaseResult.Success(rooms)
        }

        is NetworkResult.Error -> BaseResult.Error(code, message)
        is NetworkResult.Exception -> BaseResult.Exception(e.message)
    }
}

fun NetworkResult<LoginOrCreateRoomResponse>.mapToRoomDetails(): BaseResult<RoomDetails> {
    return when (this) {
        is NetworkResult.Success -> {
            if (data.isSuccess) {
                val roomDetails = data.roomDetails.mapToDomainModel()

                BaseResult.Success(roomDetails)
            } else {
                BaseResult.Error(code = data.statusCode, message = data.errorMessage)
            }
        }
        is NetworkResult.Error -> BaseResult.Error(code, message)
        is NetworkResult.Exception -> BaseResult.Exception(e.message)
    }
}