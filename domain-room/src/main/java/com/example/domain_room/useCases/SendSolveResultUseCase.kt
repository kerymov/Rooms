package com.example.domain_room.useCases

import com.example.domain_room.models.NewSolveResult
import com.example.domain_room.repository.RoomRepository
import javax.inject.Inject

class SendSolveResultUseCase @Inject constructor(
    private val repository: RoomRepository
) {

    fun invoke(result: NewSolveResult) = repository.sendSolveResult(result)
}