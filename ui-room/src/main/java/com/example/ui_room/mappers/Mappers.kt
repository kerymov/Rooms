package com.example.ui_room.mappers

import com.example.domain_room.models.NewSolveResult
import com.example.ui_common_speedcubing.mappers.mapToDomainModel
import com.example.ui_room.models.NewSolveResultUi

fun NewSolveResultUi.mapToDomainModel(): NewSolveResult {
    return NewSolveResult(
        roomId = this.roomId,
        solveNumber = this.solveNumber,
        timeInMilliseconds = this.timeInMilliseconds,
        penalty = this.penalty.mapToDomainModel()
    )
}
