package com.example.data_rooms.models

data class LoginRoomDetailsDto(
    val id: String,
    val name: String,
    val administratorName: String,
    val connectedUserNames: List<String>,
    val wasOnceConnectedUserNames: List<String>,
    val solves: List<SolveDto>,
    val settings: RoomSettingsDto,
)