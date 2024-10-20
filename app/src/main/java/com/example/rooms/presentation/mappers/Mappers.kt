package com.example.rooms.presentation.mappers

import com.example.rooms.domain.model.Room
import com.example.rooms.domain.model.User
import com.example.rooms.presentation.features.auth.models.UserUiModel
import com.example.rooms.presentation.features.main.rooms.models.Event
import com.example.rooms.presentation.features.main.rooms.models.RoomUiModel

internal fun User.mapToUiModel(): UserUiModel {
    return UserUiModel(
        name = this.username,
        token = this.token,
        expiresIn = this.expiresIn
    )
}

internal fun Room.mapToUiModel(): RoomUiModel {
    return RoomUiModel(
        id = this.id,
        name = this.roomName,
        event = Event.entries.find { it.id == this.puzzle } ?: Event.THREE_BY_THREE,
        isOpen = this.isOpen
    )
}