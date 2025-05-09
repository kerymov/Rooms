package com.example.data_room.repository

import com.example.data_common_speedcubing.mappers.mapToDomainModel
import com.example.data_common_speedcubing.models.ScrambleDto
import com.example.data_room.models.mappers.mapToDto
import com.example.data_room.dataSources.RemoteRoomDataSource
import com.example.domain_common_speedcubing.models.Scramble
import com.example.domain_common_speedcubing.models.Result
import com.example.domain_common_speedcubing.models.Solve
import com.example.domain_room.models.NewSolveResult
import com.example.domain_room.models.User
import com.example.domain_core.utils.BaseResult
import com.example.domain_room.repository.RoomRepository
import com.example.network_core.utils.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomRepositoryImpl(
    private val remoteDataSource: RemoteRoomDataSource
) : RoomRepository {

    override fun joinRoom(roomName: String, onComplete: () -> Unit) = remoteDataSource.joinRoom(roomName, onComplete)

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
