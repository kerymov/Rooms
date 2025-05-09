package com.example.domain_rooms.models

import com.example.domain_common_speedcubing.models.Solve

data class RoomDetails(
    val id: String,
    val name: String,
    val administratorName: String,
    val cachedScrambles: List<String>,
    val connectedUserNames: List<String>,
    val wasOnceConnectedUserNames: List<String>,
    val password: String?,
    val solves: List<Solve>,
    val settings: RoomSettings,
)