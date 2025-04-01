package com.example.data_onboarding.utils

import com.example.domain_core.preferences.Preferences
import com.example.network_core.utils.AuthTokenProvider
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class AuthTokenProviderImpl(
    preferences: Preferences
) : AuthTokenProvider {

    override val authToken = preferences.user
        .map { it?.authToken }
        .distinctUntilChanged()
}