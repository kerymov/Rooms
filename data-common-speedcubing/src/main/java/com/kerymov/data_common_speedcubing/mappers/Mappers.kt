package com.kerymov.data_common_speedcubing.mappers

import com.kerymov.data_common_speedcubing.models.ResultDto
import com.kerymov.data_common_speedcubing.models.ScrambleDto
import com.kerymov.data_common_speedcubing.models.SolveDto
import com.kerymov.domain_common_speedcubing.models.Event
import com.kerymov.domain_common_speedcubing.models.Penalty
import com.kerymov.domain_common_speedcubing.models.Result
import com.kerymov.domain_common_speedcubing.models.Scramble
import com.kerymov.domain_common_speedcubing.models.Solve

fun SolveDto.mapToDomainModel(): Solve {
    return Solve(
        solveNumber = this.solveNumber,
        scramble = Scramble(
            scramble = this.scramble,
            image = this.scrambledPuzzleImage?.mapToDomainModel()
        ),
        results = this.results.map { it.mapToDomainModel() }
    )
}

fun Solve.mapToDto(): SolveDto {
    return SolveDto(
        solveNumber = this.solveNumber,
        scramble = this.scramble.scramble,
        scrambledPuzzleImage = this.scramble.image?.mapToDto(),
        results = this.results.map { it.mapToDto() }
    )
}

fun ScrambleDto.mapToDomainModel(): Scramble {
    return Scramble(
        scramble = this.scramble,
        image = this.image?.mapToDomainModel()
    )
}

fun Scramble.mapToDto(): ScrambleDto {
    return ScrambleDto(
        scramble = this.scramble,
        image = this.image?.mapToDto()
    )
}

fun Scramble.Image.mapToDto(): ScrambleDto.Image {
    return ScrambleDto.Image(
        faces = this.faces.map { ScrambleDto.Face(it.colors) }
    )
}

fun ScrambleDto.Image.mapToDomainModel(): Scramble.Image {
    return Scramble.Image(
        faces = this.faces.map { Scramble.Face(it.colors) }
    )
}

fun ResultDto.mapToDomainModel(): Result {
    return Result(
        userName = this.userName,
        time = this.time,
        penalty = this.penalty.mapToPenaltyDomainModel()
    )
}

fun Result.mapToDto(): ResultDto {
    return ResultDto(
        userName = this.userName,
        time = this.time,
        penalty = this.penalty.mapToDto()
    )
}

fun Penalty.mapToDto(): Int {
    return when (this) {
        Penalty.NO_PENALTY -> 0
        Penalty.PLUS_TWO -> 1
        Penalty.DNF -> 2
    }
}


fun Int.mapToPenaltyDomainModel(): Penalty {
    return when (this) {
        0 -> Penalty.NO_PENALTY
        1 -> Penalty.PLUS_TWO
        else -> Penalty.DNF
    }
}

fun Event.mapToDto(): Int {
    return when (this) {
        Event.THREE_BY_THREE -> 3
        Event.TWO_BY_TWO -> 2
        Event.FOUR_BY_FOUR -> 4
        Event.FIVE_BY_FIVE -> 5
        Event.SIX_BY_SIX -> 6
        Event.SEVEN_BY_SEVEN -> 7
    }
}

fun Int.mapToEventDomainModel(): Event {
    return when (this) {
        3 -> Event.THREE_BY_THREE
        2 -> Event.TWO_BY_TWO
        4 -> Event.FOUR_BY_FOUR
        5 -> Event.FIVE_BY_FIVE
        6 -> Event.SIX_BY_SIX
        7 -> Event.SEVEN_BY_SEVEN
        else -> Event.THREE_BY_THREE
    }
}