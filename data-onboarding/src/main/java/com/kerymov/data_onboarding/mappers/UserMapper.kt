package com.kerymov.data_onboarding.mappers

import com.kerymov.data_onboarding.models.UserAuthResponse
import com.kerymov.domain_onboarding.models.User
import javax.inject.Inject

class UserMapper @Inject constructor() {

    val mapToDomain: (UserAuthResponse) -> User = {
        with(it) {
            User(
                username = requireNotNull(username),
                token = requireNotNull(token),
                expiresIn = expiresIn
            )
        }
    }
}