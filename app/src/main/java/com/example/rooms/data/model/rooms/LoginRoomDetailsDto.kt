package com.example.rooms.data.model.rooms

data class LoginRoomDetailsDto(
    val id: String,
    val name: String,
    val administratorName: String,
    val connectedUserNames: List<String>,
    val wasOnceConnectedUserNames: List<String>,
    val solves: List<SolveDto>,
    val settings: RoomSettingsDto,
)