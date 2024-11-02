package com.example.rooms.presentation.features.main.rooms.models

data class SettingsUi(
    val event: EventUi,
    val isOpen: Boolean = true,
    val enableSolveTimeLimit: Boolean = false,
    val usersLimit: Int = 100
)