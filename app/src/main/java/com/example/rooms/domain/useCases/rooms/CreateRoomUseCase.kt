package com.example.rooms.domain.useCases.rooms

import com.example.rooms.domain.repository.RoomsRepository

class CreateRoomUseCase(
    private val repository: RoomsRepository
) {

    suspend fun invoke() {
        return repository.createRoom()
    }
}