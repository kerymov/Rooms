package com.example.domain_rooms.useCases

import com.example.domain_core.utils.BaseResult
import com.example.domain_rooms.models.Room
import com.example.domain_rooms.repository.RoomsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRoomsUseCase @Inject constructor(
    private val repository: RoomsRepository
) {

    fun invoke(): Flow<BaseResult<List<Room>>> = repository.allRooms
}