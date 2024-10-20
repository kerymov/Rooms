package com.example.rooms.data.model.rooms.mappers

import com.example.rooms.data.model.rooms.RoomDto
import com.example.rooms.domain.model.Room

internal fun RoomDto.mapToDomainModel(): Room {
    return Room(
        id = this.id,
        roomName = this.roomName,
        puzzle = this.puzzle,
        administratorName = this.administratorName,
        isOpen = this.isOpen,
        connectedUsersCount = this.connectedUsersCount,
        maxUsersCount = this.maxUsersCount
    )
}