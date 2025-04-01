package com.example.data_rooms.models

data class CreateRoomDetailsDto(
    val id: String,
    val name: String,
    val administratorName: String,
    val cachedScrambles: List<String>,
    val connectedUserNames: List<String>,
    val wasOnceConnectedUserNames: List<String>,
    val password: String?,
    val solves: List<SolveDto>,
    val settings: RoomSettingsDto,
)