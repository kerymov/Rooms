package com.example.rooms.data.repository

import com.example.rooms.data.dataSource.room.RemoteRoomDataSource
import com.example.rooms.data.model.rooms.ScrambleDto
import com.example.rooms.data.model.rooms.mappers.mapToDomainModel
import com.example.rooms.data.model.rooms.mappers.mapToDto
import com.example.rooms.data.network.NetworkResult
import com.example.rooms.domain.model.BaseResult
import com.example.rooms.domain.model.rooms.NewSolveResult
import com.example.rooms.domain.model.rooms.Result
import com.example.rooms.domain.model.rooms.Scramble
import com.example.rooms.domain.model.rooms.Solve
import com.example.rooms.domain.model.rooms.User
import com.example.rooms.domain.repository.RoomRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomRepositoryImpl(
    private val remoteDataSource: RemoteRoomDataSource
) : RoomRepository {

    override fun joinRoom(roomName: String) = remoteDataSource.joinRoom(roomName)

    override fun leaveRoom(roomName: String) = remoteDataSource.leaveRoom(roomName)

    override fun sendSolveResult(result: NewSolveResult) = remoteDataSource.sendSolveResult(result.mapToDto())

    override fun askForNewSolve(roomId: String) = remoteDataSource.askForNewSolve(roomId)

    override val newUsers: Flow<User> = remoteDataSource.newUsers.map { User(username = it) }

    override val leftUsers: Flow<User> = remoteDataSource.leftUsers.map { User(username = it) }

    override val finishedSolves: Flow<Solve> = remoteDataSource.finishedSolves.map {
        it.mapToDomainModel()
    }

    override val newSolves: Flow<Result> = remoteDataSource.results.map {
        it.mapToDomainModel()
    }

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
