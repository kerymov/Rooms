package com.example.data_rooms.models.mappers

import com.example.data_common_speedcubing.mappers.mapToDomainModel
import com.example.data_common_speedcubing.mappers.mapToDto
import com.example.data_common_speedcubing.mappers.mapToEventDomainModel
import com.example.data_rooms.models.CreateRoomDetailsDto
import com.example.data_rooms.models.LoginRoomDetailsDto
import com.example.data_rooms.models.RoomDto
import com.example.data_rooms.models.RoomSettingsDto
import com.example.domain_rooms.models.Room
import com.example.domain_rooms.models.RoomDetails
import com.example.domain_rooms.models.RoomSettings

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