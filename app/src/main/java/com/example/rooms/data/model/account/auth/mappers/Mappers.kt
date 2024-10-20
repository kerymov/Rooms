package com.example.rooms.data.model.account.auth.mappers

import com.example.rooms.data.model.account.auth.local.UserDto
import com.example.rooms.data.model.account.auth.network.UserAuthResponseDto
import com.example.rooms.domain.model.User

internal fun UserDto.mapToDomainModel(): User {
    return User(
        username = this.username,
        token = this.token,
        expiresIn = this.expiresIn
    )
}

internal fun UserAuthResponseDto.mapToDomainModel(): User {
    return User(
        username = this.username,
        token = this.token,
        expiresIn = this.expiresIn
    )
}