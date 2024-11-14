package com.example.rooms.domain.useCases.rooms

import com.example.rooms.domain.model.BaseResult
import com.example.rooms.domain.model.rooms.RoomDetails
import com.example.rooms.domain.model.rooms.RoomSettings
import com.example.rooms.domain.repository.RoomsRepository
import kotlinx.coroutines.flow.Flow

class LoginRoomUseCase(
    private val repository: RoomsRepository
) {

    suspend fun invoke(
        name: String,
        password: String? = null,
    ): BaseResult<RoomDetails> {
        return repository.loginRoom(name = name, password = password)
    }
}