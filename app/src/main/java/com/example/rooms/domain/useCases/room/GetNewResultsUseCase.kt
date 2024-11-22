package com.example.rooms.domain.useCases.room

import com.example.rooms.domain.model.rooms.Result
import com.example.rooms.domain.repository.RoomRepository
import kotlinx.coroutines.flow.Flow

class GetNewResultsUseCase(
    private val repository: RoomRepository
) {

    fun invoke(): Flow<Result> = repository.newSolves
}