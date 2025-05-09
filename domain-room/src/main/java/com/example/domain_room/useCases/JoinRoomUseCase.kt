package com.example.domain_room.useCases

import com.example.domain_room.repository.RoomRepository

class JoinRoomUseCase(
    private val repository: RoomRepository
) {

    fun invoke(roomName: String, onComplete: () -> Unit) = repository.joinRoom(roomName, onComplete)
}