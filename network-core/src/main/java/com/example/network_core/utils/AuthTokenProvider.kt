package com.example.network_core.utils

import kotlinx.coroutines.flow.Flow

interface AuthTokenProvider {

    val authToken: Flow<String?>
}