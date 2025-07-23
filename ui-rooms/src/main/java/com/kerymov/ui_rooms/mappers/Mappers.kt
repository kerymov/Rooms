package com.kerymov.ui_rooms.mappers

import com.kerymov.domain_rooms.models.Room
import com.kerymov.domain_rooms.models.RoomDetails
import com.kerymov.domain_rooms.models.RoomSettings
import com.kerymov.ui_common_speedcubing.mappers.mapToUiModel
import com.kerymov.ui_common_speedcubing.mappers.mapToDomainModel
import com.kerymov.ui_common_speedcubing.models.EventUi
import com.kerymov.ui_rooms.models.RoomDetailsUi
import com.kerymov.ui_rooms.models.RoomUi
import com.kerymov.ui_rooms.models.SettingsUi

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
