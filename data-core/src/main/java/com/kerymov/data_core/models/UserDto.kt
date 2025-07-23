package com.kerymov.data_core.models

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val username: String? = null,
    val authToken: String? = null,
    val expiresIn: Int? = null,
)