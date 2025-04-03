package com.example.rooms.domain.useCases.room

import com.example.domain_rooms.models.Scramble
import com.example.rooms.domain.model.BaseResult
import com.example.rooms.domain.repository.RoomRepository

class GetScrambleUseCase(
    private val roomRepository: RoomRepository
) {

    suspend fun invoke(puzzle: Int): BaseResult<Scramble> {
        return roomRepository.getScramble(puzzle)
    }
}