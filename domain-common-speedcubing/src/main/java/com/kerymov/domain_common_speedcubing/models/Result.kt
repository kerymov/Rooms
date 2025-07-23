package com.kerymov.domain_common_speedcubing.models

data class Result(
    val userName: String,
    val time: Long,
    val penalty: Penalty
)