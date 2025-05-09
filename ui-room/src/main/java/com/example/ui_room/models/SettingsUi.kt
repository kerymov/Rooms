package com.example.ui_room.models

import com.example.ui_common_speedcubing.models.EventUi
import kotlinx.serialization.Serializable

@Serializable
data class SettingsUi(
    val event: EventUi,
    val isOpen: Boolean = true,
    val enableSolveTimeLimit: Boolean = false,
    val usersLimit: Int = 100
)