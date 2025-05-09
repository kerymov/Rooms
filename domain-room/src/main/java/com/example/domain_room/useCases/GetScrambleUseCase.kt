package com.example.domain_room.useCases

import com.example.domain_common_speedcubing.models.Scramble
import com.example.domain_core.utils.BaseResult
import com.example.domain_room.repository.RoomRepository

class GetScrambleUseCase(
    private val roomRepository: RoomRepository
) {

    suspend fun invoke(puzzle: Int): BaseResult<Scramble> {
        return roomRepository.getScramble(puzzle)
    }
}