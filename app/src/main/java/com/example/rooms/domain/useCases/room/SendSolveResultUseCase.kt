package com.example.rooms.domain.useCases.room

import com.example.rooms.domain.model.rooms.NewSolveResult
import com.example.rooms.domain.model.rooms.Solve
import com.example.rooms.domain.repository.RoomRepository

class SendSolveResultUseCase(
    private val repository: RoomRepository
) {

    fun invoke(result: NewSolveResult) = repository.sendSolveResult(result)
}