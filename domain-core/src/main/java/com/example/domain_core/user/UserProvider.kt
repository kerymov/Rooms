package com.example.domain_core.user

import kotlinx.coroutines.flow.Flow

interface UserProvider {

    val username: Flow<String?>
}