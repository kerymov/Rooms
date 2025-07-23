package com.kerymov.domain_room.useCases

import com.kerymov.domain_room.models.NewSolveResult
import com.kerymov.domain_room.repository.RoomRepository
import javax.inject.Inject

class SendSolveResultUseCase @Inject constructor(
    private val repository: RoomRepository
) {

    fun invoke(result: NewSolveResult) = repository.sendSolveResult(result)
}