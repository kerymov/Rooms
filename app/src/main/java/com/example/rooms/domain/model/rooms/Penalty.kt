package com.example.rooms.domain.model.rooms

import kotlinx.serialization.Serializable

@Serializable
enum class Penalty {
    NO_PENALTY,
    PLUS_TWO,
    DNF
}