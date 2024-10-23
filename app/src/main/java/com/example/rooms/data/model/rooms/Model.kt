package com.example.rooms.data.model.rooms

data class Model(
    val administratorName: String,
    val cachedScrambles: List<String>,
    val connectedUserNames: List<String>,
    val id: String,
    val name: String,
    val password: String,
    val settings: RoomSettingsDto,
    val solves: List<Any>,
    val wasOnceConnectedUserNames: List<String>
)