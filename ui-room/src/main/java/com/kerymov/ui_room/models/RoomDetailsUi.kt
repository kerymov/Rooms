package com.kerymov.ui_room.models

import com.kerymov.ui_common_speedcubing.models.SolveUi
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