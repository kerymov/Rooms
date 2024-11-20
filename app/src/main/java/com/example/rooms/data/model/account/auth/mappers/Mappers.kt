package com.example.rooms.data.model.account.auth.mappers

import com.example.rooms.data.model.account.auth.local.UserDto
import com.example.rooms.data.model.account.auth.network.UserAuthResponse
import com.example.rooms.domain.model.auth.User

internal fun UserDto.mapToDomainModel(): User {
    return User(
        username = this.username,
        token = this.token,
        expiresIn = this.expiresIn
    )
}

internal fun UserAuthResponse.mapToDomainModel(): User {
    return User(
        username = this.username,
        token = this.token,
        expiresIn = this.expiresIn
    )
}