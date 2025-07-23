package com.kerymov.domain_room.useCases

import com.kerymov.domain_common_speedcubing.models.Scramble
import com.kerymov.domain_core.utils.BaseResult
import com.kerymov.domain_room.repository.RoomRepository
import javax.inject.Inject

class GetScrambleUseCase @Inject constructor(
    private val roomRepository: RoomRepository
) {

    suspend fun invoke(puzzle: Int): BaseResult<Scramble> {
        return roomRepository.getScramble(puzzle)
    }
}