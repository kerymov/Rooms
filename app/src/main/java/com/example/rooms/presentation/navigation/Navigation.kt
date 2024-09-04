package com.example.rooms.presentation.navigation

sealed class NavSection(val name: String) {

    sealed class Screen(val name: String)
    data object Auth : NavSection("section_auth") {
        data object SignIn : Screen("screen_sign_in")
        data object SignUp : Screen("screen_sign_up")
    }

    data object Rooms : NavSection("section_rooms") {
        data object Rooms : Screen("screen_rooms")
        data object Room : Screen("screen_room")
        data object Results : Screen("screen_results")
    }

    data object Profile : NavSection("section_profile") {
        data object Profile : Screen("screen_profile")
    }
}