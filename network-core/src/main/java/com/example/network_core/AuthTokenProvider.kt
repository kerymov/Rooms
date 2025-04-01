package com.example.network_core

import kotlinx.coroutines.flow.Flow

interface AuthTokenProvider {

    val authToken: Flow<String?>
}