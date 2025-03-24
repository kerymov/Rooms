package com.example.data_onboarding.utils

import com.example.data_onboarding.models.UserAuthResponse
import com.example.domain_onboarding.models.User
import javax.inject.Inject

class UserMapper @Inject constructor() {

    val mapToDomain: (UserAuthResponse) -> User = {
        with(it) {
            User(username, token, expiresIn)
        }
    }
}