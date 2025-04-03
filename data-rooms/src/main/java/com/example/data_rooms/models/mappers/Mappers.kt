package com.example.data_rooms.models.mappers

import com.example.data_rooms.models.ResultDto
import com.example.data_rooms.models.CreateRoomDetailsDto
import com.example.data_rooms.models.LoginRoomDetailsDto
import com.example.data_rooms.models.NewSolveResultDto
import com.example.data_rooms.models.RoomDto
import com.example.data_rooms.models.RoomSettingsDto
import com.example.data_rooms.models.ScrambleDto
import com.example.data_rooms.models.SolveDto
import com.example.domain_rooms.models.Event
import com.example.domain_rooms.models.NewSolveResult
import com.example.domain_rooms.models.Penalty
import com.example.domain_rooms.models.Room
import com.example.domain_rooms.models.RoomDetails
import com.example.domain_rooms.models.RoomSettings
import com.example.domain_rooms.models.Scramble
import com.example.domain_rooms.models.Solve
import com.example.domain_rooms.models.Result

fun RoomDto.mapToDomainModel(): Room {
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

fun CreateRoomDetailsDto.mapToDomainModel(): RoomDetails {
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

fun LoginRoomDetailsDto.mapToDomainModel(): RoomDetails {
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

fun RoomSettings.mapToDto(): RoomSettingsDto {
    return RoomSettingsDto(
        puzzle = this.event.mapToDto(),
        isOpen = this.isOpen,
        enableSolveTimeLimit = this.enableSolveTimeLimit,
        usersLimit = this.usersLimit
    )
}

fun RoomSettingsDto.mapToDomainModel(): RoomSettings {
    return RoomSettings(
        event = this.puzzle.mapToEventDomainModel(),
        isOpen = this.isOpen,
        enableSolveTimeLimit = this.enableSolveTimeLimit,
        usersLimit = this.usersLimit
    )
}

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

fun NewSolveResult.mapToDto(): NewSolveResultDto {
    return NewSolveResultDto(
        roomId = this.roomId,
        solveNumber = this.solveNumber,
        timeInMilliseconds = this.timeInMilliseconds.toInt(),
        penalty = this.penalty.mapToDto()
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