package com.kerymov.domain_core.model

data class User(
    val username: String? = null,
    val authToken: String? = null,
    val expiresIn: Int? = null,
)
