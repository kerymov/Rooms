package com.example.rooms.presentation.mappers

import com.example.rooms.domain.model.auth.User
import com.example.rooms.domain.model.rooms.Event
import com.example.rooms.domain.model.rooms.NewSolveResult
import com.example.rooms.domain.model.rooms.Penalty
import com.example.rooms.domain.model.rooms.Result
import com.example.rooms.domain.model.rooms.Room
import com.example.rooms.domain.model.rooms.RoomDetails
import com.example.rooms.domain.model.rooms.RoomSettings
import com.example.rooms.domain.model.rooms.Scramble
import com.example.rooms.domain.model.rooms.Solve
import com.example.rooms.presentation.features.auth.models.UserUiModel
import com.example.rooms.presentation.features.main.rooms.models.EventUi
import com.example.rooms.presentation.features.main.rooms.models.NewSolveResultUi
import com.example.rooms.presentation.features.main.rooms.models.PenaltyUi
import com.example.rooms.presentation.features.main.rooms.models.ResultUi
import com.example.rooms.presentation.features.main.rooms.models.RoomDetailsUi
import com.example.rooms.presentation.features.main.rooms.models.RoomUi
import com.example.rooms.presentation.features.main.rooms.models.ScrambleUi
import com.example.rooms.presentation.features.main.rooms.models.SettingsUi
import com.example.rooms.presentation.features.main.rooms.models.SolveUi

internal fun User.mapToUiModel(): UserUiModel {
    return UserUiModel(
        name = this.username,
        token = this.token,
        expiresIn = this.expiresIn
    )
}

internal fun Room.mapToUiModel(): RoomUi {
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

internal fun RoomDetails.mapToUiModel(): RoomDetailsUi {
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

internal fun SettingsUi.mapToDomainModel(): RoomSettings {
    return RoomSettings(
        event = this.event.mapToDomainModel(),
        isOpen = this.isOpen,
        enableSolveTimeLimit = this.enableSolveTimeLimit,
        usersLimit = this.usersLimit
    )
}

internal fun RoomSettings.mapToUiModel(): SettingsUi {
    return SettingsUi(
        event = this.event.mapToUiModel(),
        isOpen = this.isOpen,
        enableSolveTimeLimit = this.enableSolveTimeLimit,
        usersLimit = this.usersLimit
    )
}

internal fun EventUi.mapToDomainModel(): Event {
    return Event.entries.find { this.name == it.name } ?: Event.THREE_BY_THREE
}

internal fun Event.mapToUiModel(): EventUi {
    return when (this) {
        Event.THREE_BY_THREE -> EventUi.THREE_BY_THREE
        Event.TWO_BY_TWO -> EventUi.TWO_BY_TWO
        Event.FOUR_BY_FOUR -> EventUi.FOUR_BY_FOUR
        Event.FIVE_BY_FIVE -> EventUi.FIVE_BY_FIVE
        Event.SIX_BY_SIX -> EventUi.SIX_BY_SIX
        Event.SEVEN_BY_SEVEN -> EventUi.SEVEN_BY_SEVEN
    }
}

internal fun Solve.mapToUiModel(): SolveUi {
    return SolveUi(
        solveNumber = this.solveNumber,
        scramble = this.scramble.mapToUiModel(),
        results = this.results.map { it.mapToUiModel() }
    )
}

internal fun SolveUi.mapToDomainModel(): Solve {
    return Solve(
        solveNumber = this.solveNumber,
        scramble = this.scramble.mapToDomainModel(),
        results = this.results.map { it.mapToDomainModel() }
    )
}

internal fun ScrambleUi.mapToDomainModel(): Scramble {
    return Scramble(
        scramble = this.scramble,
        image =  this.image?.mapToDomainModel()
    )
}

internal fun ScrambleUi.Image.mapToDomainModel(): Scramble.Image {
    return Scramble.Image(
        faces = this.faces.map { Scramble.Face(it.colors) }
    )
}

internal fun NewSolveResultUi.mapToDomainModel(): NewSolveResult {
    return NewSolveResult(
        roomId = this.roomId,
        solveNumber = this.solveNumber,
        timeInMilliseconds = this.timeInMilliseconds,
        penalty = this.penalty.mapToDomainModel()
    )
}

internal fun Scramble.mapToUiModel(): ScrambleUi {
    return ScrambleUi(
        scramble = this.scramble,
        image =  this.image?.mapToUiModel()
    )
}

internal fun Scramble.Image.mapToUiModel(): ScrambleUi.Image {
    return ScrambleUi.Image(
        faces = this.faces.map { ScrambleUi.Face(it.colors) }
    )
}

internal fun Result.mapToUiModel(): ResultUi {
    return ResultUi(
        userName = this.userName,
        time = this.time,
        penalty = this.penalty.mapToUiModel()
    )
}

internal fun Penalty.mapToUiModel(): PenaltyUi {
    return when (this) {
        Penalty.NO_PENALTY -> PenaltyUi.NO_PENALTY
        Penalty.DNF -> PenaltyUi.DNF
        Penalty.PLUS_TWO -> PenaltyUi.PLUS_TWO
    }
}

internal fun ResultUi.mapToDomainModel(): Result {
    return Result(
        userName = this.userName,
        time = this.time,
        penalty = this.penalty.mapToDomainModel()
    )
}

internal fun PenaltyUi.mapToDomainModel(): Penalty {
    return when (this) {
        PenaltyUi.NO_PENALTY -> Penalty.NO_PENALTY
        PenaltyUi.DNF -> Penalty.DNF
        PenaltyUi.PLUS_TWO -> Penalty.PLUS_TWO
    }
}