package com.example.rooms.data.model.rooms.mappers

import com.example.rooms.data.model.rooms.RoomDetailsDto
import com.example.rooms.data.model.rooms.RoomDto
import com.example.rooms.data.model.rooms.RoomSettingsDto
import com.example.rooms.domain.model.rooms.Room
import com.example.rooms.domain.model.rooms.RoomDetails
import com.example.rooms.domain.model.rooms.RoomSettings

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

internal fun RoomDetailsDto.mapToDomainModel(): RoomDetails {
    return RoomDetails(
        id = this.id,
        name = this.name,
        administratorName = this.administratorName,
        cachedScrambles = this.cachedScrambles,
        connectedUserNames = this.connectedUserNames,
        wasOnceConnectedUserNames = this.wasOnceConnectedUserNames,
        password = this.password,
        solves = this.solves,
        settings = this.settings.mapToDomainModel()
    )
}

internal fun RoomSettings.mapToDto(): RoomSettingsDto {
    return RoomSettingsDto(
        puzzle = this.event,
        isOpen = this.isOpen,
        enableSolveTimeLimit = this.enableSolveTimeLimit,
        usersLimit = this.usersLimit
    )
}

internal fun RoomSettingsDto.mapToDomainModel(): RoomSettings {
    return RoomSettings(
        event = this.puzzle,
        isOpen = this.isOpen,
        enableSolveTimeLimit = this.enableSolveTimeLimit,
        usersLimit = this.usersLimit
    )
}