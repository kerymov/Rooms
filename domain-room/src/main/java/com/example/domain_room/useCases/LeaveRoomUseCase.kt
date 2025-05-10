package com.example.domain_room.useCases

import com.example.domain_room.repository.RoomRepository
import javax.inject.Inject

class LeaveRoomUseCase @Inject constructor(
    private val repository: RoomRepository
) {

    fun invoke(roomName: String) = repository.leaveRoom(roomName)
}