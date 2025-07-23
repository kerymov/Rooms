package com.kerymov.data_rooms.repository

import com.kerymov.data_rooms.dataSources.RemoteRoomsDataSource
import com.kerymov.data_rooms.models.RoomDto
import com.kerymov.data_rooms.models.mappers.mapToDomainModel
import com.kerymov.data_rooms.models.mappers.mapToDto
import com.kerymov.data_rooms.models.requests.CreateRoomRequest
import com.kerymov.data_rooms.models.requests.LoginRoomRequest
import com.kerymov.data_rooms.models.responses.CreateRoomResponse
import com.kerymov.data_rooms.models.responses.LoginRoomResponse
import com.kerymov.domain_core.utils.BaseResult
import com.kerymov.domain_rooms.models.Room
import com.kerymov.domain_rooms.models.RoomDetails
import com.kerymov.domain_rooms.models.RoomSettings
import com.kerymov.domain_rooms.repository.RoomsRepository
import com.kerymov.network_core.utils.NetworkResult
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

        return remoteDataSource.createRoom(createRoomRequest).mapToCreateRoomDetails()
    }

    override suspend fun loginRoom(
        name: String, password: String?
    ): BaseResult<RoomDetails> {
        val loginRoomRequest = LoginRoomRequest(
            roomName = name,
            roomPassword = password
        )

        return remoteDataSource.loginRoom(loginRoomRequest).mapToLoginRoomDetails()
    }

    override suspend fun deleteRoom(id: String): BaseResult<Boolean> {
        return remoteDataSource.deleteRoom(id).mapToBaseResult()
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

fun NetworkResult<CreateRoomResponse>.mapToCreateRoomDetails(): BaseResult<RoomDetails> {
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

fun NetworkResult<LoginRoomResponse>.mapToLoginRoomDetails(): BaseResult<RoomDetails> {
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

fun NetworkResult<Boolean>.mapToBaseResult(): BaseResult<Boolean> {
    return when (this) {
        is NetworkResult.Success -> BaseResult.Success(data)
        is NetworkResult.Error -> BaseResult.Error(code, message)
        is NetworkResult.Exception -> BaseResult.Exception(e.message)
    }
}