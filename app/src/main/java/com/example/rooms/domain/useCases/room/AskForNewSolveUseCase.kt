package com.example.rooms.domain.useCases.room

import com.example.rooms.domain.repository.RoomRepository

class AskForNewSolveUseCase(
    private val repository: RoomRepository
) {

    fun invoke(roomId: String) = repository.askForNewSolve(roomId)
}