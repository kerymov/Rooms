package com.example.rooms.presentation.navigation

import com.example.rooms.presentation.features.main.rooms.models.RoomDetailsUi
import kotlinx.serialization.Serializable

@Serializable
sealed interface NavModule

@Serializable
sealed interface Screen

@Serializable
data object Auth : NavModule {
    @Serializable
    data object SignIn : Screen
    @Serializable
    data object SignUp : Screen
}

@Serializable
data object Main : NavModule {

    @Serializable
    data object Rooms : Screen
    @Serializable
    data object Profile : Screen
}

@Serializable
data object Room : NavModule {

    @Serializable
    data class RoomMain(val details: String? = null) : Screen
    @Serializable
    data object Results : Screen
}