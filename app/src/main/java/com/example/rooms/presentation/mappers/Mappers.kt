package com.example.rooms.presentation.mappers

import com.example.rooms.domain.model.rooms.Room
import com.example.rooms.domain.model.User
import com.example.rooms.domain.model.rooms.Event
import com.example.rooms.domain.model.rooms.RoomDetails
import com.example.rooms.domain.model.rooms.RoomSettings
import com.example.rooms.presentation.features.auth.models.UserUiModel
import com.example.rooms.presentation.features.main.rooms.models.EventUi
import com.example.rooms.presentation.features.main.rooms.models.RoomUi
import com.example.rooms.presentation.features.main.rooms.models.SettingsUi

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
        isOpen = this.isOpen
    )
}

internal fun RoomDetails.mapToRoomUiModel(): RoomUi {
    return RoomUi(
        id = this.id,
        name = this.name,
        event = EventUi.entries.find { it.name == this.settings.event.name } ?: EventUi.THREE_BY_THREE,
        isOpen = this.settings.isOpen
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

internal fun EventUi.mapToDomainModel(): Event {
    return Event.entries.find { this.name == it.name } ?: Event.THREE_BY_THREE
}