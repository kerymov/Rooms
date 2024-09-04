package com.example.rooms.presentation.features.auth.screens

internal enum class Field(
    val placeholder: String,
    val onEmptyErrorMessage: String
) {
    USERNAME("Username", "Username can not be blank"),
    PASSWORD("Password", "Password can not be blank"),
    REPEAT_PASSWORD("Repeat password", "Repeated password can not be blank")
}