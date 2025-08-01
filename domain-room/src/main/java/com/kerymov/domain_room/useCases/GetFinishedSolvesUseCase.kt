package com.kerymov.domain_room.useCases

import com.kerymov.domain_room.repository.RoomRepository
import com.kerymov.domain_common_speedcubing.models.Solve
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFinishedSolvesUseCase @Inject constructor(
    private val repository: RoomRepository
) {

    fun invoke(): Flow<Solve> = repository.finishedSolves
}