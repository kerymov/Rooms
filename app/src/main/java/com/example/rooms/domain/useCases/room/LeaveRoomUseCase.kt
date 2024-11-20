package com.example.rooms.domain.useCases.room

import com.example.rooms.domain.repository.RoomRepository

class LeaveRoomUseCase(
    private val repository: RoomRepository
) {

    fun invoke(roomName: String) = repository.leaveRoom(roomName)
}