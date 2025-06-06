package com.example.data_core.mappers

import com.example.data_core.models.UserDto
import com.example.domain_core.model.User

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