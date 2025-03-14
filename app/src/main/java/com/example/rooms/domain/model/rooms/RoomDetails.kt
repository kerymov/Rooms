package com.example.rooms.domain.model.rooms

data class RoomDetails(
    val id: String,
    val name: String,
    val administratorName: String,
    val cachedScrambles: List<String>,
    val connectedUserNames: List<String>,
    val wasOnceConnectedUserNames: List<String>,
    val password: String?,
    val solves: List<Solve>,
    val settings: RoomSettings,
)