package com.example.rooms.presentation

import android.content.Context
import android.content.SharedPreferences
import com.example.rooms.presentation.viewModels.RoomViewModel
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
import com.example.rooms.presentation.navigation.Screen
import com.example.rooms.presentation.screens.ResultsScreen
import com.example.rooms.presentation.screens.RoomScreen
import com.example.rooms.presentation.screens.RoomsScreen
import com.example.rooms.presentation.screens.SignInScreen
import com.example.rooms.presentation.screens.SignUpScreen
import com.example.rooms.presentation.viewModels.SignInViewModel
import com.example.rooms.presentation.viewModels.RoomsViewModel

@Composable
fun RoomsApp(
    signInViewModel: SignInViewModel = viewModel(),
    roomsViewModel: RoomsViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.SIGN_IN.name
) {
    val roomsUiState by roomsViewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(route = Screen.SIGN_IN.name) {
            SignInScreen(
                onSignInClick = { login, password ->
                    signInViewModel.signIn(login = login, password = password)
                },
                onSignInSuccess = {
                    navController.navigate(Screen.ROOMS.name)
                },
                onSignUpClick = {
                    navController.navigate(Screen.SIGN_UP.name)
                },
                signInViewModel = signInViewModel
            )
        }
        composable(route = Screen.SIGN_UP.name) {
            SignUpScreen(
                onSignUpClick = { login, password, confirmedPassword ->
                    var result = false

                    signInViewModel.signUp(
                        login = login,
                        password = password,
                        passwordConfirm = confirmedPassword,
                        onSuccess = {
                            navController.navigate(Screen.ROOMS.name)
                            result = true
                        },
                        onFailure = {
                            result = false
                        }
                    )

                    return@SignUpScreen result
                },
                onSignInClick = {
                    navController.navigate(Screen.SIGN_IN.name)
                }
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
            roomId?.let {
                roomsViewModel.getRoomById(it)
            } ?: return@composable

            val roomViewModel = viewModel<RoomViewModel>(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return RoomViewModel(roomId) as T
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
