package com.example.rooms.data.model.rooms.mappers

import com.example.rooms.data.model.rooms.RoomDto
import com.example.rooms.domain.model.rooms.Room

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