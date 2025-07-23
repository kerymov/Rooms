package com.kerymov.data_core.utils

import com.kerymov.domain_core.preferences.Preferences
import com.kerymov.domain_core.user.UserProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class UserProviderImpl(
    preferences: Preferences
) : UserProvider {

    override val username: Flow<String?> = preferences.user
        .map { it?.username }
        .distinctUntilChanged()
}