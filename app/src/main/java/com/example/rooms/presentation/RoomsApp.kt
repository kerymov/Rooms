package com.example.rooms.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.rooms.presentation.ui.navigation.NavSection
import com.example.rooms.presentation.ui.navigation.navigate
import com.example.rooms.presentation.ui.screens.RoomsScreen
import com.example.rooms.presentation.ui.screens.auth.SignInScreen
import com.example.rooms.presentation.ui.screens.auth.SignUpScreen
import com.example.rooms.presentation.ui.viewModels.RoomsViewModel
import com.example.rooms.presentation.ui.viewModels.AuthViewModel

@Composable
fun RoomsApp(
    navController: NavHostController,
    roomsViewModel: RoomsViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel(),
    startNavSection: NavSection,
) = Scaffold { contentPadding ->
    NavHost(
        navController = navController,
        startDestination = startNavSection.name,
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        navigation(
            route = NavSection.Auth.name,
            startDestination = NavSection.Auth.SignIn.name
        ) {
            composable(route = NavSection.Auth.SignIn.name) {
                SignInScreen(
                    onSignInSuccess = {
                        navController.navigate(section = NavSection.Rooms, param = null) {
                            popUpTo(NavSection.Auth.name) {
                                inclusive = true
                            }
                        }
                    },
                    onGoToSignUpClick = {
                        authViewModel.resetUiState()
                        navController.navigate(screen = NavSection.Auth.SignUp, param = null) {
                            popUpTo(0)
                        }
                    },
                    authViewModel = authViewModel
                )
            }
            composable(route = NavSection.Auth.SignUp.name) {
                SignUpScreen(
                    onSignUpSuccess = {
                        navController.navigate(section = NavSection.Rooms, param = null) {
                            popUpTo(NavSection.Auth.name) {
                                inclusive = true
                            }
                        }
                    },
                    onGoToSignInClick = {
                        authViewModel.resetUiState()
                        navController.navigate(screen = NavSection.Auth.SignIn, param = null) {
                            popUpTo(0)
                        }
                    },
                    authViewModel = authViewModel
                )
            }
        }
        navigation(
            route = NavSection.Rooms.name,
            startDestination = NavSection.Rooms.Rooms.name
        ) {
            composable(route = NavSection.Rooms.Rooms.name) {
                RoomsScreen(
                    navController = navController,
                    roomsViewModel = roomsViewModel
                )
            }
            composable(route = NavSection.Rooms.Room.name) {

            }
            composable(route = NavSection.Rooms.Results.name) {

            }
        }
        navigation(
            route = NavSection.Profile.name,
            startDestination = NavSection.Profile.Profile.name
        ) {
            composable(route = NavSection.Profile.Profile.name) {

            }
        }
    }
}

@Preview
@Composable
private fun RoomsAppPreview() {
//    RoomsApp(listOf())
}

//    composable(
//        route = Screen.ROOM.name + "/{roomId}",
//        arguments = listOf(navArgument("roomId") { type = NavType.StringType })
//    ) { backStackEntry ->
//        val roomId = backStackEntry.arguments?.getString("roomId")
//        val room = roomId?.let {
//            roomsViewModel.getRoomById(it)
//        } ?: return@composable
//
//        val roomViewModel = viewModel<RoomViewModel>(
//            factory = object : ViewModelProvider.Factory {
//                @Suppress("UNCHECKED_CAST")
//                override fun <T : ViewModel> create(modelClass: Class<T>): T {
//                    return RoomViewModel(room) as T
//                }
//            }
//        )
//        RoomScreen(
//            navController = rememberNavController(),
//            signInViewModel = signInViewModel,
//            roomViewModel = roomViewModel,
//        )
//    }
//    composable(
//        route = Screen.RESULTS.name + "/{roomId}",
//        arguments = listOf(navArgument("roomId") { type = NavType.StringType })
//    ) { backStackEntry ->
//        val roomId = backStackEntry.arguments?.getString("roomId")
//        roomId?.let {
//            roomsViewModel.getRoomById(it)
//        } ?: return@composable
//
//        val room = roomsUiState.currentRoom ?: return@composable
//        ResultsScreen(
//            navController = navController,
//            roomName = room.roomName,
//        )
//    }