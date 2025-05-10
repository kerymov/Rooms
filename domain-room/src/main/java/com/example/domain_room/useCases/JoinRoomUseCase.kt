package com.example.domain_room.useCases

import com.example.domain_room.repository.RoomRepository
import javax.inject.Inject

class JoinRoomUseCase @Inject constructor(
    private val repository: RoomRepository
) {

    fun invoke(roomName: String, onComplete: () -> Unit) = repository.joinRoom(roomName, onComplete)
}