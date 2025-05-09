package com.example.domain_common_speedcubing.models

data class Result(
    val userName: String,
    val time: Long,
    val penalty: Penalty
)