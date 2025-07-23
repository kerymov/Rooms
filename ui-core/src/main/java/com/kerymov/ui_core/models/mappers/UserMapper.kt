package com.kerymov.ui_core.models.mappers

import com.kerymov.domain_core.model.User
import com.kerymov.ui_core.models.UserUi
import javax.inject.Inject

class UserMapper @Inject constructor() {

    val mapToDomain: (UserUi) -> User = {
        User(it.username)
    }

    val mapFromDomain: (User) -> UserUi = {
        UserUi(it.username!!)
    }
}