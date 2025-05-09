package com.example.domain_room.useCases

import com.example.domain_room.repository.RoomRepository

class AskForNewSolveUseCase(
    private val repository: RoomRepository
) {

    fun invoke(roomId: String) = repository.askForNewSolve(roomId)
}