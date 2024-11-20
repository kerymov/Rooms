package com.example.rooms.data.repository

import com.example.rooms.data.dataSource.room.RemoteRoomDataSource
import com.example.rooms.data.model.rooms.ScrambleDto
import com.example.rooms.data.model.rooms.mappers.mapToDomainModel
import com.example.rooms.data.network.NetworkResult
import com.example.rooms.domain.model.BaseResult
import com.example.rooms.domain.model.rooms.Scramble
import com.example.rooms.domain.model.rooms.User
import com.example.rooms.domain.repository.RoomRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomRepositoryImpl(
    private val remoteDataSource: RemoteRoomDataSource
) : RoomRepository {

    override fun joinRoom(roomName: String) = remoteDataSource.joinRoom(roomName)

    override fun leaveRoom(roomName: String) = remoteDataSource.leaveRoom(roomName)

//    override val users: Flow<User> = combine(
//        remoteDataSource.users,
//        remoteDataSource.leftUsers
//    ) { usersList, leftUsersList -> usersList.filter { user -> user !in leftUsersList } }
//        .map { User(username = it) }

    override val newUsers: Flow<User> = remoteDataSource.newUsers.map { User(username = it) }

    override suspend fun getScramble(puzzle: Int): BaseResult<Scramble> {
        return remoteDataSource.getScramble(puzzle).toBaseResult()
    }
}

private fun NetworkResult<ScrambleDto>.toBaseResult(): BaseResult<Scramble> {
    return when(this) {
        is NetworkResult.Success -> BaseResult.Success(data.mapToDomainModel())
        is NetworkResult.Error -> BaseResult.Error(code, message)
        is NetworkResult.Exception -> BaseResult.Exception(e.message)
    }
}
