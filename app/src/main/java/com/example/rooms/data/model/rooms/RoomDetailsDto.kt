package com.example.rooms.data.model.rooms

data class RoomDetailsDto(
    val id: String,
    val name: String,
    val administratorName: String,
    val cachedScrambles: List<String>,
    val connectedUserNames: List<String>,
    val wasOnceConnectedUserNames: List<String>,
    val password: String,
    val solves: List<SolveDto>,
    val settings: RoomSettingsDto,
)