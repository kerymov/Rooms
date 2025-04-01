package com.example.ui_rooms.models

import kotlinx.serialization.Serializable

@Serializable
data class RoomDetailsUi(
    val id: String,
    val name: String,
    val password: String?,
    val administratorName: String,
    val cachedScrambles: List<String>,
    val connectedUserNames: List<String>,
    val wasOnceConnectedUserNames: List<String>,
    val solves: List<SolveUi>,
    val settings: SettingsUi,
)