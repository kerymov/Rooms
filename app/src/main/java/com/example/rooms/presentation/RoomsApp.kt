package com.example.rooms.presentation

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
import com.example.rooms.presentation.screens.RoomScreen
import com.example.rooms.presentation.screens.RoomsScreen
import com.example.rooms.presentation.screens.SignInScreen
import com.example.rooms.presentation.screens.SignUpScreen
import com.example.rooms.presentation.viewModels.AccountViewModel
import com.example.rooms.presentation.viewModels.RoomsViewModel

@Composable
fun RoomsApp(
    accountViewModel: AccountViewModel = viewModel(),
    roomsViewModel: RoomsViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
) {
    val roomsUiState by roomsViewModel.uiState.collectAsState()
    NavHost(
        navController = navController,
        startDestination = Screen.ROOMS.name,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(route = Screen.SIGN_IN.name) {
            SignInScreen(
                onSignInClick = { login, password ->
                    var result = false

                    accountViewModel.login(
                        login = login,
                        password = password,
                        onSuccess = {
                            navController.navigate(Screen.ROOMS.name)
                            result = true
                        },
                        onFailure = {
                            result = false
                        }
                    )

                    return@SignInScreen result
                },
                onSignUpClick = {
                    navController.navigate(Screen.SIGN_UP.name)
                }
            )
        }
        composable(route = Screen.SIGN_UP.name) {
            SignUpScreen(
                onSignUpClick = { login, password -> true },
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
                },
                onCreateRoomClick = {}
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

            val room = roomsUiState.currentRoom ?: return@composable
            val roomViewModel = viewModel<RoomViewModel>(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return RoomViewModel(room) as T
                    }
                }
            )
            RoomScreen(
                roomViewModel = roomViewModel,
                onNavigationButtonClick = { navController.popBackStack() },
                onActionButtonClick = { navController.navigate(Screen.RESULTS.name) }
            )
        }
        composable(route = Screen.RESULTS.name) {

        }
    }
}

@Preview
@Composable
private fun RoomsAppPreview() {
//    RoomsApp(listOf())
}
