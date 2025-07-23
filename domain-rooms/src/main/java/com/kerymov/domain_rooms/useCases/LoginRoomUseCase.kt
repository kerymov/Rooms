package com.kerymov.domain_rooms.useCases

import com.kerymov.domain_core.utils.BaseResult
import com.kerymov.domain_rooms.models.RoomDetails
import com.kerymov.domain_rooms.repository.RoomsRepository
import javax.inject.Inject

class LoginRoomUseCase @Inject constructor(
    private val repository: RoomsRepository
) {

    suspend fun invoke(
        name: String,
        password: String? = null,
    ): BaseResult<RoomDetails> {
        return repository.loginRoom(name = name, password = password)
    }
}