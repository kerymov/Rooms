package com.example.rooms.presentation.features.main.rooms.models

data class RoomUiModel(
    val id: String,
    val name: String,
    val event: Event,
    val isOpen: Boolean
)
