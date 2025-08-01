package com.kerymov.domain_room.useCases

import com.kerymov.domain_room.repository.RoomRepository
import javax.inject.Inject

class AskForNewSolveUseCase @Inject constructor(
    private val repository: RoomRepository
) {

    fun invoke(roomId: String) = repository.askForNewSolve(roomId)
}