package com.example.data_onboarding.utils

import com.example.domain_core.preferences.Preferences
import com.example.network_core.AuthTokenProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthTokenProviderImpl(
    preferences: Preferences
) : AuthTokenProvider {

    override val authToken = preferences.user
        .map { it?.authToken }
        .distinctUntilChanged()
}