package com.example.rooms.domain.useCases.room

import com.example.domain_rooms.models.NewSolveResult
import com.example.rooms.domain.repository.RoomRepository

class SendSolveResultUseCase(
    private val repository: RoomRepository
) {

    fun invoke(result: NewSolveResult) = repository.sendSolveResult(result)
}