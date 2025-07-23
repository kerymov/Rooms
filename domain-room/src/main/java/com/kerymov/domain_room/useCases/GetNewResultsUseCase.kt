package com.kerymov.domain_room.useCases

import com.kerymov.domain_room.repository.RoomRepository
import com.kerymov.domain_common_speedcubing.models.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNewResultsUseCase @Inject constructor(
    private val repository: RoomRepository
) {

    fun invoke(): Flow<Result> = repository.newSolves
}