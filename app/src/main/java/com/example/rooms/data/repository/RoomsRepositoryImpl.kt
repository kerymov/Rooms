package com.example.rooms.data.repository

import com.example.rooms.data.dataSource.rooms.RemoteRoomsDataSource
import com.example.rooms.data.network.NetworkResult
import com.example.rooms.data.network.handleApi
import com.example.rooms.domain.model.BaseResult
import com.example.rooms.domain.model.Room
import com.example.rooms.domain.repository.RoomsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RoomsRepositoryImpl(
    private val remoteDataSource: RemoteRoomsDataSource
) : RoomsRepository {

    override suspend fun getRooms(): Flow<BaseResult<List<Room>>> {
        val result = handleApi { remoteDataSource.getRooms() }

        return flow {
            when(result) {
                is NetworkResult.Success -> {
                    val rooms = result.data.map { roomDto ->
                        Room(
                            id = roomDto.id,
                            roomName = roomDto.roomName,
                            puzzle = roomDto.puzzle,
                            administratorName = roomDto.administratorName,
                            isOpen = roomDto.isOpen,
                            connectedUsersCount = roomDto.connectedUsersCount,
                            maxUsersCount = roomDto.maxUsersCount
                        )
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

    override suspend fun createRoom() {
        TODO("Not yet implemented")
    }
}