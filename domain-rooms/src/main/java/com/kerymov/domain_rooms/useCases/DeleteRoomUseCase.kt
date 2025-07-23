package com.kerymov.domain_rooms.useCases

import com.kerymov.domain_core.utils.BaseResult
import com.kerymov.domain_rooms.repository.RoomsRepository
import javax.inject.Inject

class DeleteRoomUseCase @Inject constructor(
    private val repository: RoomsRepository
) {

    suspend fun invoke(id: String): BaseResult<Boolean> {
        return repository.deleteRoom(id = id)
    }
}