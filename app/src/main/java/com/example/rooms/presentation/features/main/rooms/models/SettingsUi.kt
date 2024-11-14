package com.example.rooms.presentation.features.main.rooms.models

import kotlinx.serialization.Serializable

@Serializable
data class SettingsUi(
    val event: EventUi,
    val isOpen: Boolean = true,
    val enableSolveTimeLimit: Boolean = false,
    val usersLimit: Int = 100
)