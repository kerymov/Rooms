package com.example.rooms.data.model.rooms.mappers

import com.example.rooms.data.model.rooms.ResultDto
import com.example.rooms.data.model.rooms.CreateRoomDetailsDto
import com.example.rooms.data.model.rooms.LoginRoomDetailsDto
import com.example.rooms.data.model.rooms.NewSolveResultDto
import com.example.rooms.data.model.rooms.RoomDto
import com.example.rooms.data.model.rooms.RoomSettingsDto
import com.example.rooms.data.model.rooms.ScrambleDto
import com.example.rooms.data.model.rooms.SolveDto
import com.example.rooms.domain.model.rooms.Event
import com.example.rooms.domain.model.rooms.NewSolveResult
import com.example.rooms.domain.model.rooms.Penalty
import com.example.rooms.domain.model.rooms.Result
import com.example.rooms.domain.model.rooms.Room
import com.example.rooms.domain.model.rooms.RoomDetails
import com.example.rooms.domain.model.rooms.RoomSettings
import com.example.rooms.domain.model.rooms.Scramble
import com.example.rooms.domain.model.rooms.Solve

internal fun RoomDto.mapToDomainModel(): Room {
    return Room(
        id = this.id,
        name = this.roomName,
        event = this.puzzle,
        administratorName = this.administratorName,
        isOpen = this.isOpen,
        connectedUsersCount = this.connectedUsersCount,
        maxUsersCount = this.maxUsersCount
    )
}

internal fun CreateRoomDetailsDto.mapToDomainModel(): RoomDetails {
    return RoomDetails(
        id = this.id,
        name = this.name,
        administratorName = this.administratorName,
        cachedScrambles = this.cachedScrambles,
        connectedUserNames = this.connectedUserNames,
        wasOnceConnectedUserNames = this.wasOnceConnectedUserNames,
        password = this.password,
        solves = this.solves.map { it.mapToDomainModel() },
        settings = this.settings.mapToDomainModel()
    )
}

internal fun LoginRoomDetailsDto.mapToDomainModel(): RoomDetails {
    return RoomDetails(
        id = this.id,
        name = this.name,
        administratorName = this.administratorName,
        cachedScrambles = listOf(),
        connectedUserNames = this.connectedUserNames,
        wasOnceConnectedUserNames = this.wasOnceConnectedUserNames,
        password = null,
        solves = this.solves.map { it.mapToDomainModel() },
        settings = this.settings.mapToDomainModel()
    )
}

internal fun RoomSettings.mapToDto(): RoomSettingsDto {
    return RoomSettingsDto(
        puzzle = this.event.mapToDto(),
        isOpen = this.isOpen,
        enableSolveTimeLimit = this.enableSolveTimeLimit,
        usersLimit = this.usersLimit
    )
}

internal fun RoomSettingsDto.mapToDomainModel(): RoomSettings {
    return RoomSettings(
        event = this.puzzle.mapToEventDomainModel(),
        isOpen = this.isOpen,
        enableSolveTimeLimit = this.enableSolveTimeLimit,
        usersLimit = this.usersLimit
    )
}

internal fun SolveDto.mapToDomainModel(): Solve {
    return Solve(
        solveNumber = this.solveNumber,
        scramble = Scramble(
            scramble = this.scramble,
            image = this.scrambledPuzzleImage?.mapToDomainModel()
        ),
        results = this.results.map { it.mapToDomainModel() }
    )
}

internal fun Solve.mapToDto(): SolveDto {
    return SolveDto(
        solveNumber = this.solveNumber,
        scramble = this.scramble.scramble,
        scrambledPuzzleImage = this.scramble.image?.mapToDto(),
        results = this.results.map { it.mapToDto() }
    )
}

internal fun NewSolveResult.mapToDto(): NewSolveResultDto {
    return NewSolveResultDto(
        roomId = this.roomId,
        solveNumber = this.solveNumber,
        timeInMilliseconds = this.timeInMilliseconds.toInt(),
        penalty = this.penalty.mapToDto()
    )
}

internal fun ScrambleDto.mapToDomainModel(): Scramble {
    return Scramble(
        scramble = this.scramble,
        image = this.image?.mapToDomainModel()
    )
}

internal fun Scramble.mapToDto(): ScrambleDto {
    return ScrambleDto(
        scramble = this.scramble,
        image = this.image?.mapToDto()
    )
}

internal fun Scramble.Image.mapToDto(): ScrambleDto.Image {
    return ScrambleDto.Image(
        faces = this.faces.map { ScrambleDto.Face(it.colors) }
    )
}

internal fun ScrambleDto.Image.mapToDomainModel(): Scramble.Image {
    return Scramble.Image(
        faces = this.faces.map { Scramble.Face(it.colors) }
    )
}

internal fun ResultDto.mapToDomainModel(): Result {
    return Result(
        userName = this.userName,
        time = this.time,
        penalty = this.penalty.mapToPenaltyDomainModel()
    )
}

internal fun Result.mapToDto(): ResultDto {
    return ResultDto(
        userName = this.userName,
        time = this.time,
        penalty = this.penalty.mapToDto()
    )
}

internal fun Penalty.mapToDto(): Int {
    return when (this) {
        Penalty.NO_PENALTY -> 0
        Penalty.DNF -> 1
        Penalty.PLUS_TWO -> 2
    }
}


internal fun Int.mapToPenaltyDomainModel(): Penalty {
    return when (this) {
        0 -> Penalty.NO_PENALTY
        2 -> Penalty.PLUS_TWO
        else -> Penalty.DNF
    }
}

internal fun Event.mapToDto(): Int {
    return when (this) {
        Event.THREE_BY_THREE -> 3
        Event.TWO_BY_TWO -> 2
        Event.FOUR_BY_FOUR -> 4
        Event.FIVE_BY_FIVE -> 5
        Event.SIX_BY_SIX -> 6
        Event.SEVEN_BY_SEVEN -> 7
    }
}

internal fun Int.mapToEventDomainModel(): Event {
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