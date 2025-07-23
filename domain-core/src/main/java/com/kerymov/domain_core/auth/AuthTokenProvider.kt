package com.kerymov.domain_core.auth

import kotlinx.coroutines.flow.Flow

interface AuthTokenProvider {

    val authToken: Flow<String?>
}