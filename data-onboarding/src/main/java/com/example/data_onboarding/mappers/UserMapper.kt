package com.example.data_onboarding.mappers

import com.example.data_onboarding.models.UserAuthResponse
import com.example.domain_onboarding.models.User
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