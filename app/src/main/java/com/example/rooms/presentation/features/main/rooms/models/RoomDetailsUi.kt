package com.example.rooms.presentation.features.main.rooms.models

data class RoomDetailsUi(
    val id: String,
    val name: String,
    val administratorName: String,
    val cachedScrambles: List<String>,
    val connectedUserNames: List<String>,
    val wasOnceConnectedUserNames: List<String>,
    val password: String,
    val solves: List<SolveUi>,
    val settings: SettingsUi,
)