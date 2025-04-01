package com.example.domain_rooms.models

import com.example.domain_rooms.models.Penalty

data class Result(
    val userName: String,
    val time: Long,
    val penalty: Penalty
)