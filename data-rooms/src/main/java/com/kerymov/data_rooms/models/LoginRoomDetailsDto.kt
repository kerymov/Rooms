package com.kerymov.data_rooms.models

import com.kerymov.data_common_speedcubing.models.SolveDto

data class LoginRoomDetailsDto(
    val id: String,
    val name: String,
    val administratorName: String,
    val connectedUserNames: List<String>,
    val wasOnceConnectedUserNames: List<String>,
    val solves: List<SolveDto>,
    val settings: RoomSettingsDto,
)