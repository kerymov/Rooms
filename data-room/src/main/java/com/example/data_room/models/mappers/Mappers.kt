package com.example.data_room.models.mappers

import com.example.data_common_speedcubing.mappers.mapToDto
import com.example.data_room.models.NewSolveResultDto
import com.example.domain_room.models.NewSolveResult

fun NewSolveResult.mapToDto(): NewSolveResultDto {
    return NewSolveResultDto(
        roomId = this.roomId,
        solveNumber = this.solveNumber,
        timeInMilliseconds = this.timeInMilliseconds.toInt(),
        penalty = this.penalty.mapToDto()
    )
}