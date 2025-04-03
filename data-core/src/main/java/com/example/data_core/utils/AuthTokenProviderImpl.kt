package com.example.data_core.utils

import com.example.domain_core.auth.AuthTokenProvider
import com.example.domain_core.preferences.Preferences
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class AuthTokenProviderImpl(
    preferences: Preferences
) : AuthTokenProvider {

    override val authToken = preferences.user
        .map { it?.authToken }
        .distinctUntilChanged()
}