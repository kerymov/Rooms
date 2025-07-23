package com.kerymov.data_core.mappers

import com.kerymov.data_core.models.UserDto
import com.kerymov.domain_core.model.User

class UserMapper {

    val mapToDomain: (UserDto) -> User = {
        with(it) {
            User(username, authToken, expiresIn)
        }
    }

    val mapFromDomain: (User) -> UserDto = {
        with(it) {
            UserDto(username, authToken, expiresIn)
        }
    }
}