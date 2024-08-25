package com.example.rooms.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rooms.presentation.ui.navigation.Screen
import com.example.rooms.presentation.ui.screens.ResultsScreen
import com.example.rooms.presentation.ui.screens.RoomScreen
import com.example.rooms.presentation.ui.screens.RoomsScreen
import com.example.rooms.presentation.ui.screens.SignInScreen
import com.example.rooms.presentation.ui.screens.SignUpScreen
import com.example.rooms.presentation.ui.viewModels.RoomViewModel
import com.example.rooms.presentation.ui.viewModels.RoomsViewModel
import com.example.rooms.presentation.ui.viewModels.SignInViewModel
import com.example.rooms.presentation.ui.viewModels.SignUpViewModel

@Composable
fun RoomsApp(
    signInViewModel: SignInViewModel = viewModel(),
    signUpViewModel: SignUpViewModel = viewModel(),
    roomsViewModel: RoomsViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
    startDestination: String
) {
    val roomsUiState by roomsViewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(route = Screen.SIGN_IN.name) {
            SignInScreen(
                onSignInSuccess = {
                    navController.navigate(Screen.ROOMS.name)
                },
                onGoToSignUpClick = {
                    navController.navigate(Screen.SIGN_UP.name)
                },
                signInViewModel = signInViewModel
            )
        }
        composable(route = Screen.SIGN_UP.name) {
            SignUpScreen(
                onSignUpSuccess = {
                    navController.navigate(Screen.ROOMS.name)
                },
                onGoToSignInClick = {
                    navController.navigate(Screen.SIGN_IN.name)
                },
                signUpViewModel = signUpViewModel,
            )
        }
        composable(route = Screen.ROOMS.name) {
            RoomsScreen(
                roomsViewModel = roomsViewModel,
                onRoomCardClick = { room ->
                    navController.navigate(Screen.ROOM.name + "/${room.id}")
                }
            )
        }
        composable(
            route = Screen.ROOM.name + "/{roomId}",
            arguments = listOf(navArgument("roomId") { type = NavType.StringType })
        ) { backStackEntry ->
            val roomId = backStackEntry.arguments?.getString("roomId")
            val room = roomId?.let {
                roomsViewModel.getRoomById(it)
            } ?: return@composable

            val roomViewModel = viewModel<RoomViewModel>(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return RoomViewModel(room) as T
                    }
                }
            )
            RoomScreen(
                signInViewModel = signInViewModel,
                roomViewModel = roomViewModel,
                onNavigationButtonClick = { navController.popBackStack() },
                onActionButtonClick = { navController.navigate(Screen.RESULTS.name + "/${roomId}") }
            )
        }
        composable(
            route = Screen.RESULTS.name + "/{roomId}",
            arguments = listOf(navArgument("roomId") { type = NavType.StringType })
        ) { backStackEntry ->
            val roomId = backStackEntry.arguments?.getString("roomId")
            roomId?.let {
                roomsViewModel.getRoomById(it)
            } ?: return@composable

            val room = roomsUiState.currentRoom ?: return@composable
            ResultsScreen(
                roomName = room.roomName,
                onNavigationButtonClick = { navController.popBackStack() }
            )
        }
    }
}

@Preview
@Composable
private fun RoomsAppPreview() {
//    RoomsApp(listOf())
}
