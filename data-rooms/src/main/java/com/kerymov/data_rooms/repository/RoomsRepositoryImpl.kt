package com.kerymov.data_rooms.repository

import com.kerymov.data_core.mappers.NetworkResultMapper
import com.kerymov.data_rooms.dataSources.RemoteRoomsDataSource
import com.kerymov.data_rooms.models.mappers.mapToDomainModel
import com.kerymov.data_rooms.models.mappers.mapToDto
import com.kerymov.data_rooms.models.requests.CreateRoomRequest
import com.kerymov.data_rooms.models.requests.LoginRoomRequest
import com.kerymov.domain_core.exceptions.CommonException
import com.kerymov.domain_core.utils.BaseResult
import com.kerymov.domain_rooms.exceptions.RoomsException
import com.kerymov.domain_rooms.models.Room
import com.kerymov.domain_rooms.models.RoomDetails
import com.kerymov.domain_rooms.models.RoomSettings
import com.kerymov.domain_rooms.repository.RoomsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.collections.map

class RoomsRepositoryImpl(
    private val remoteDataSource: RemoteRoomsDataSource,
    private val networkResultMapper: NetworkResultMapper
) : RoomsRepository {

    override val allRooms: Flow<BaseResult<List<Room>>> = remoteDataSource.allRooms
        .map { networkResult ->
            networkResultMapper.mapToBaseResult(networkResult) { roomDtos ->
                roomDtos.map { roomDto -> roomDto.mapToDomainModel() }
            }
        }

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

        val networkResult = remoteDataSource.createRoom(createRoomRequest)
        val baseResult = networkResultMapper.mapAlwaysSuccessfulResponseToBaseResult(
            networkResult = networkResult,
            transform = { response ->
                response.roomDetails.mapToDomainModel()
            },
            handleDomainError = { result ->
                when (result.data.errorMessage) {
                    "Room with provided name already exists" -> BaseResult.Error(
                        RoomsException.RoomWithSameNameAlreadyExistsException
                    )
                    "Invalid room name" -> BaseResult.Error(
                        RoomsException.InvalidRoomNameException
                    )
                    "Invalid room password" -> BaseResult.Error(
                        RoomsException.InvalidRoomPasswordException
                    )
                    else -> BaseResult.Error(
                        CommonException.UnknownException
                    )
                }
            }
        )

        return baseResult
    }

    override suspend fun loginRoom(
        name: String, password: String?
    ): BaseResult<RoomDetails> {
        val loginRoomRequest = LoginRoomRequest(
            roomName = name,
            roomPassword = password
        )

        val networkResult = remoteDataSource.loginRoom(loginRoomRequest)
        val baseResult = networkResultMapper.mapAlwaysSuccessfulResponseToBaseResult(
            networkResult = networkResult,
            transform = { response ->
                response.roomDetails.mapToDomainModel()
            },
            handleDomainError = {
                BaseResult.Error(RoomsException.RoomPasswordIsWrongException)
            }
        )

        return baseResult
    }

    override suspend fun deleteRoom(id: String): BaseResult<Boolean> {
        val networkResult = remoteDataSource.deleteRoom(id)
        val baseResult = networkResultMapper.mapToBaseResult(networkResult) { it }

        return baseResult
    }
}
