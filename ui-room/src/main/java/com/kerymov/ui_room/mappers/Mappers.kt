package com.kerymov.ui_room.mappers

import com.kerymov.domain_room.models.NewSolveResult
import com.kerymov.ui_common_speedcubing.mappers.mapToDomainModel
import com.kerymov.ui_room.models.NewSolveResultUi

fun NewSolveResultUi.mapToDomainModel(): NewSolveResult {
    return NewSolveResult(
        roomId = this.roomId,
        solveNumber = this.solveNumber,
        timeInMilliseconds = this.timeInMilliseconds,
        penalty = this.penalty.mapToDomainModel()
    )
}
