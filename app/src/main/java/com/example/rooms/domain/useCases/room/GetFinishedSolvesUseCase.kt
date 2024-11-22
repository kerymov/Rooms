package com.example.rooms.domain.useCases.room

import com.example.rooms.domain.model.rooms.Solve
import com.example.rooms.domain.repository.RoomRepository
import kotlinx.coroutines.flow.Flow

class GetFinishedSolvesUseCase(
    private val repository: RoomRepository
) {

    fun invoke(): Flow<Solve> = repository.finishedSolves
}