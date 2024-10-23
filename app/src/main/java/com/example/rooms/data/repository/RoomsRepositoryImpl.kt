package com.example.rooms.data.repository

import com.example.rooms.data.dataSource.rooms.RemoteRoomsDataSource
import com.example.rooms.data.model.rooms.mappers.mapToDomainModel
import com.example.rooms.data.network.NetworkResult
import com.example.rooms.domain.model.BaseResult
import com.example.rooms.domain.model.rooms.Room
import com.example.rooms.domain.repository.RoomsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

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

    override suspend fun createRoom() {
        TODO("Not yet implemented")
    }
}