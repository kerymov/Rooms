package com.kerymov.domain_rooms.useCases

import com.kerymov.domain_core.utils.BaseResult
import com.kerymov.domain_rooms.models.Room
import com.kerymov.domain_rooms.repository.RoomsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRoomsUseCase @Inject constructor(
    private val repository: RoomsRepository
) {

    fun invoke(): Flow<BaseResult<List<Room>>> = repository.allRooms
}