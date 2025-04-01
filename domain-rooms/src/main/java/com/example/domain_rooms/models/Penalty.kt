package com.example.domain_rooms.models

import kotlinx.serialization.Serializable

@Serializable
enum class Penalty {
    NO_PENALTY,
    PLUS_TWO,
    DNF
}