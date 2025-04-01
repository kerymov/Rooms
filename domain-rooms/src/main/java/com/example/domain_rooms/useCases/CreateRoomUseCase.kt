package com.example.domain_rooms.useCases

import com.example.domain_core.utils.BaseResult
import com.example.domain_rooms.models.RoomDetails
import com.example.domain_rooms.models.RoomSettings
import com.example.domain_rooms.repository.RoomsRepository
import javax.inject.Inject

class CreateRoomUseCase @Inject constructor(
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