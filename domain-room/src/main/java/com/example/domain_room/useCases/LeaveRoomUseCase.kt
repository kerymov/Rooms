package com.example.domain_room.useCases

import com.example.domain_room.repository.RoomRepository

class LeaveRoomUseCase(
    private val repository: RoomRepository
) {

    fun invoke(roomName: String) = repository.leaveRoom(roomName)
}