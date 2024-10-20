package com.example.rooms.presentation.mappers

import com.example.rooms.domain.model.User
import com.example.rooms.presentation.features.auth.models.UserUiModel

internal fun User.mapToUiModel(): UserUiModel {
    return UserUiModel(
        name = this.username,
        token = this.token,
        expiresIn = this.expiresIn
    )
}