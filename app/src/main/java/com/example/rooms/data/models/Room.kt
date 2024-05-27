package com.example.rooms.data.models

data class Room(
    val id: String,
    val settings: RoomSettings,
    val name: String,
    val administratorName: String,
    val wasOnceConnectedUserNames: List<String>,
    val password: String?,
    val connectedUserNames: List<String>,
    val solves: List<Solve>,
    val cachedScrambles: List<String>
)