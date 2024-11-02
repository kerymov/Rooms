package com.example.rooms.data.repository

import com.example.rooms.data.dataSource.rooms.RemoteRoomsDataSource
import com.example.rooms.data.model.rooms.CreateRoomRequest
import com.example.rooms.data.model.rooms.mappers.mapToDomainModel
import com.example.rooms.data.model.rooms.mappers.mapToDto
import com.example.rooms.data.network.NetworkResult
import com.example.rooms.domain.model.BaseResult
import com.example.rooms.domain.model.rooms.Room
import com.example.rooms.domain.model.rooms.RoomDetails
import com.example.rooms.domain.model.rooms.RoomSettings
import com.example.rooms.domain.repository.RoomsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.collections.map

class RoomsRepositoryImpl(
    private val remoteDataSource: RemoteRoomsDataSource
) : RoomsRepository {

    override suspend fun getRooms(): Flow<BaseResult<List<Room>>> {
        val result = remoteDataSource.getRooms()

        return flow {
            when(result) {
                is NetworkResult.Success -> {
                    val rooms = result.data.map { roomDto ->
                        roomDto.mapToDomainModel()
                    }

                    emit(BaseResult.Success(rooms))
                }
                is NetworkResult.Error -> {
                    emit(BaseResult.Error(result.code, result.message))
                }
                is NetworkResult.Exception -> {
                    emit(BaseResult.Exception(result.e.message))
                }
            }
        }
    }

    override suspend fun createRoom(name: String, password: String?, settings: RoomSettings): Flow<BaseResult<RoomDetails>> {
        val createRoomRequest = CreateRoomRequest(roomName = name, roomPassword = password, settings = settings.mapToDto())
        val result = remoteDataSource.createRoom(createRoomRequest)

        return flow {
            when(result) {
                is NetworkResult.Success -> {
                    val roomDetails = result.data.mapToDomainModel()

                    emit(BaseResult.Success(roomDetails))
                }
                is NetworkResult.Error -> {
                    emit(BaseResult.Error(result.code, result.message))
                }
                is NetworkResult.Exception -> {
                    emit(BaseResult.Exception(result.e.message))
                }
            }
        }
    }
}