package com.example.domain_rooms.useCases

import com.example.domain_core.utils.BaseResult
import com.example.domain_rooms.repository.RoomsRepository
import javax.inject.Inject

class DeleteRoomUseCase @Inject constructor(
    private val repository: RoomsRepository
) {

    suspend fun invoke(id: String): BaseResult<Boolean> {
        return repository.deleteRoom(id = id)
    }
}