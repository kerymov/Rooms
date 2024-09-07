package com.example.rooms.presentation.navigation

sealed class NavModule(val route: String) {

    sealed class Submodule(val route: String)
    sealed class Screen(val route: String)

    data object Auth : NavModule("module_auth") {
        data object SignIn : Screen("screen_sign_in")
        data object SignUp : Screen("screen_sign_up")
    }

    data object Main : NavModule("module_main") {

        data object Rooms : Submodule("submodule_rooms") {
            data object Rooms : Screen("screen_rooms")
            data object Room : Screen("screen_room")
            data object Results : Screen("screen_results")
        }

        data object Profile : Submodule("submodule_profile") {
            data object Profile : Screen("screen_profile")
        }
    }
}