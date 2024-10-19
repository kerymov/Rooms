package com.example.rooms.domain.useCases.rooms

import com.example.rooms.domain.model.BaseResult
import com.example.rooms.domain.model.Room
import com.example.rooms.domain.repository.RoomsRepository
import kotlinx.coroutines.flow.Flow

class GetRoomsUseCase(
    private val repository: RoomsRepository
) {

    suspend fun invoke(): Flow<BaseResult<List<Room>>> {
        return repository.getRooms()
    }
}