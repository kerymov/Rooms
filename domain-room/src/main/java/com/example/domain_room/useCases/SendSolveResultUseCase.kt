package com.example.domain_room.useCases

import com.example.domain_room.models.NewSolveResult
import com.example.domain_room.repository.RoomRepository

class SendSolveResultUseCase(
    private val repository: RoomRepository
) {

    fun invoke(result: NewSolveResult) = repository.sendSolveResult(result)
}