package com.kerymov.ui_common_speedcubing.mappers

import com.kerymov.domain_common_speedcubing.models.*
import com.kerymov.ui_common_speedcubing.models.ResultUi
import com.kerymov.ui_common_speedcubing.models.SolveUi
import com.kerymov.ui_common_speedcubing.models.EventUi
import com.kerymov.ui_common_speedcubing.models.PenaltyUi
import com.kerymov.ui_common_speedcubing.models.ScrambleUi

fun EventUi.mapToDomainModel(): Event {
    return Event.entries.find { this.name == it.name } ?: Event.THREE_BY_THREE
}

fun Event.mapToUiModel(): EventUi {
    return when (this) {
        Event.THREE_BY_THREE -> EventUi.THREE_BY_THREE
        Event.TWO_BY_TWO -> EventUi.TWO_BY_TWO
        Event.FOUR_BY_FOUR -> EventUi.FOUR_BY_FOUR
        Event.FIVE_BY_FIVE -> EventUi.FIVE_BY_FIVE
        Event.SIX_BY_SIX -> EventUi.SIX_BY_SIX
        Event.SEVEN_BY_SEVEN -> EventUi.SEVEN_BY_SEVEN
    }
}

fun Solve.mapToUiModel(): SolveUi {
    return SolveUi(
        solveNumber = this.solveNumber,
        scramble = this.scramble.mapToUiModel(),
        results = this.results.map { it.mapToUiModel() }
    )
}

fun SolveUi.mapToDomainModel(): Solve {
    return Solve(
        solveNumber = this.solveNumber,
        scramble = this.scramble.mapToDomainModel(),
        results = this.results.map { it.mapToDomainModel() }
    )
}

fun ScrambleUi.mapToDomainModel(): Scramble {
    return Scramble(
        scramble = this.scramble,
        image =  this.image?.mapToDomainModel()
    )
}

fun ScrambleUi.Image.mapToDomainModel(): Scramble.Image {
    return Scramble.Image(
        faces = this.faces.map { Scramble.Face(it.colors) }
    )
}

fun Scramble.mapToUiModel(): ScrambleUi {
    return ScrambleUi(
        scramble = this.scramble,
        image =  this.image?.mapToUiModel()
    )
}

fun Scramble.Image.mapToUiModel(): ScrambleUi.Image {
    return ScrambleUi.Image(
        faces = this.faces.map { ScrambleUi.Face(it.colors) }
    )
}

fun Result.mapToUiModel(): ResultUi {
    return ResultUi(
        userName = this.userName,
        time = this.time,
        penalty = this.penalty.mapToUiModel()
    )
}

fun Penalty.mapToUiModel(): PenaltyUi {
    return when (this) {
        Penalty.NO_PENALTY -> PenaltyUi.NO_PENALTY
        Penalty.DNF -> PenaltyUi.DNF
        Penalty.PLUS_TWO -> PenaltyUi.PLUS_TWO
    }
}

fun ResultUi.mapToDomainModel(): Result {
    return Result(
        userName = this.userName,
        time = this.time,
        penalty = this.penalty.mapToDomainModel()
    )
}

fun PenaltyUi.mapToDomainModel(): Penalty {
    return when (this) {
        PenaltyUi.NO_PENALTY -> Penalty.NO_PENALTY
        PenaltyUi.DNF -> Penalty.DNF
        PenaltyUi.PLUS_TWO -> Penalty.PLUS_TWO
    }
}