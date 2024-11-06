package com.example.rooms.domain.useCases.rooms

import com.example.rooms.domain.model.BaseResult
import com.example.rooms.domain.model.rooms.RoomDetails
import com.example.rooms.domain.model.rooms.RoomSettings
import com.example.rooms.domain.repository.RoomsRepository
import kotlinx.coroutines.flow.Flow

class CreateRoomUseCase(
    private val repository: RoomsRepository
) {

    suspend fun invoke(
        name: String,
        password: String? = null,
        settings: RoomSettings
    ): BaseResult<RoomDetails> {
        return repository.createRoom(name = name, password = password, settings = settings)
    }
}