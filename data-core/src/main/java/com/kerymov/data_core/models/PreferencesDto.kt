package com.kerymov.data_core.models

import kotlinx.serialization.Serializable

@Serializable
data class PreferencesDto(
    val user: UserDto? = null
)
