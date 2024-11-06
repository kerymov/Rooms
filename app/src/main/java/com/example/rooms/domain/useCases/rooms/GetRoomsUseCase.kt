package com.example.rooms.domain.useCases.rooms

import com.example.rooms.domain.model.BaseResult
import com.example.rooms.domain.model.rooms.Room
import com.example.rooms.domain.repository.RoomsRepository
import kotlinx.coroutines.flow.Flow

class GetRoomsUseCase(
    private val repository: RoomsRepository
) {

    fun invoke(): Flow<BaseResult<List<Room>>> = repository.allRooms
}