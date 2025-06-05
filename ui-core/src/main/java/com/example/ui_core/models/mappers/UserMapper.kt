package com.example.ui_core.models.mappers

import com.example.domain_core.model.User
import com.example.ui_core.models.UserUi
import javax.inject.Inject

class UserMapper @Inject constructor() {

    val mapToDomain: (UserUi) -> User = {
        User(it.username)
    }

    val mapFromDomain: (User) -> UserUi = {
        UserUi(it.username!!)
    }
}