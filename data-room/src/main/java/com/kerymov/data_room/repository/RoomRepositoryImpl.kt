package com.kerymov.data_room.repository

import com.kerymov.data_common_speedcubing.mappers.mapToDomainModel
import com.kerymov.data_common_speedcubing.models.ScrambleDto
import com.kerymov.data_room.models.mappers.mapToDto
import com.kerymov.data_room.dataSources.RemoteRoomDataSource
import com.kerymov.domain_common_speedcubing.models.Scramble
import com.kerymov.domain_common_speedcubing.models.Result
import com.kerymov.domain_common_speedcubing.models.Solve
import com.kerymov.domain_room.models.NewSolveResult
import com.kerymov.domain_room.models.User
import com.kerymov.domain_core.utils.BaseResult
import com.kerymov.domain_room.repository.RoomRepository
import com.kerymov.network_core.utils.NetworkResult
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
