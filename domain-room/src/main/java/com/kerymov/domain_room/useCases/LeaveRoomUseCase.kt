package com.kerymov.domain_room.useCases

import com.kerymov.domain_room.repository.RoomRepository
import javax.inject.Inject

class LeaveRoomUseCase @Inject constructor(
    private val repository: RoomRepository
) {

    fun invoke(roomName: String) = repository.leaveRoom(roomName)
}