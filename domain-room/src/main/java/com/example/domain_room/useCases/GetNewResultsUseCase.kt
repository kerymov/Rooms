package com.example.domain_room.useCases

import com.example.domain_room.repository.RoomRepository
import com.example.domain_common_speedcubing.models.Result
import kotlinx.coroutines.flow.Flow

class GetNewResultsUseCase(
    private val repository: RoomRepository
) {

    fun invoke(): Flow<Result> = repository.newSolves
}