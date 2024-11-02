package com.example.rooms.presentation.features.main.rooms.models

data class RoomUi(
    val id: String,
    val name: String,
    val event: EventUi,
    val isOpen: Boolean
)
