package com.example.rooms.domain.useCases.rooms

import com.example.rooms.domain.model.BaseResult
import com.example.rooms.domain.repository.RoomsRepository

class DeleteRoomUseCase(
    private val repository: RoomsRepository
) {

    suspend fun invoke(id: String): BaseResult<Boolean> {
        return repository.deleteRoom(id = id)
    }
}