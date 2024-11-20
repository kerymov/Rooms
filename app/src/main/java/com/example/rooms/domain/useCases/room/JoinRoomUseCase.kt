package com.example.rooms.domain.useCases.room

import com.example.rooms.domain.repository.RoomRepository

class JoinRoomUseCase(
    private val repository: RoomRepository
) {

    fun invoke(roomName: String) = repository.joinRoom(roomName)
}