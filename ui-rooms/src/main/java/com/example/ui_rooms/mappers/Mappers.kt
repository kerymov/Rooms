package com.example.ui_rooms.mappers

import com.example.domain_rooms.models.Room
import com.example.domain_rooms.models.Event
import com.example.domain_rooms.models.NewSolveResult
import com.example.domain_rooms.models.Penalty
import com.example.domain_rooms.models.RoomDetails
import com.example.domain_rooms.models.RoomSettings
import com.example.domain_rooms.models.Scramble
import com.example.domain_rooms.models.Solve
import com.example.domain_rooms.models.Result
import com.example.ui_rooms.models.EventUi
import com.example.ui_rooms.models.NewSolveResultUi
import com.example.ui_rooms.models.PenaltyUi
import com.example.ui_rooms.models.ResultUi
import com.example.ui_rooms.models.RoomDetailsUi
import com.example.ui_rooms.models.RoomUi
import com.example.ui_rooms.models.ScrambleUi
import com.example.ui_rooms.models.SettingsUi
import com.example.ui_rooms.models.SolveUi

fun Room.mapToUiModel(): RoomUi {
    return RoomUi(
        id = this.id,
        name = this.name,
        event = EventUi.entries.find { it.id == this.event } ?: EventUi.THREE_BY_THREE,
        administratorName = this.administratorName,
        connectedUsersCount = this.connectedUsersCount,
        maxUsersCount = this.maxUsersCount,
        isOpen = this.isOpen
    )
}

fun RoomDetails.mapToUiModel(): RoomDetailsUi {
    return RoomDetailsUi(
        id = this.id,
        name = this.name,
        password = this.password,
        administratorName = this.administratorName,
        cachedScrambles = this.cachedScrambles,
        connectedUserNames = this.connectedUserNames,
        wasOnceConnectedUserNames = this.wasOnceConnectedUserNames,
        solves = this.solves.map { it.mapToUiModel() },
        settings = this.settings.mapToUiModel()
    )
}

fun SettingsUi.mapToDomainModel(): RoomSettings {
    return RoomSettings(
        event = this.event.mapToDomainModel(),
        isOpen = this.isOpen,
        enableSolveTimeLimit = this.enableSolveTimeLimit,
        usersLimit = this.usersLimit
    )
}

fun RoomSettings.mapToUiModel(): SettingsUi {
    return SettingsUi(
        event = this.event.mapToUiModel(),
        isOpen = this.isOpen,
        enableSolveTimeLimit = this.enableSolveTimeLimit,
        usersLimit = this.usersLimit
    )
}

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

fun NewSolveResultUi.mapToDomainModel(): NewSolveResult {
    return NewSolveResult(
        roomId = this.roomId,
        solveNumber = this.solveNumber,
        timeInMilliseconds = this.timeInMilliseconds,
        penalty = this.penalty.mapToDomainModel()
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