package com.example.rooms.presentation.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.rooms.presentation.ui.screens.RoomsScreen
import com.example.rooms.presentation.ui.screens.auth.SignInScreen
import com.example.rooms.presentation.ui.screens.auth.SignUpScreen
import com.example.rooms.presentation.ui.viewModels.RoomsViewModel
import com.example.rooms.presentation.ui.viewModels.SignInViewModel
import com.example.rooms.presentation.ui.viewModels.SignUpViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: Screen,
    signInViewModel: SignInViewModel,
    signUpViewModel: SignUpViewModel,
    roomsViewModel: RoomsViewModel,
    modifier: Modifier = Modifier
) = NavHost(
    navController = navController,
    startDestination = startDestination.name,
    modifier = modifier.fillMaxSize()
) {
    composable(route = Screen.SIGN_IN.name) {
        SignInScreen(
            navController = navController,
            signInViewModel = signInViewModel
        )
    }
    composable(route = Screen.SIGN_UP.name) {
        SignUpScreen(
            navController = navController,
            signUpViewModel = signUpViewModel,
        )
    }
    composable(route = Screen.ROOMS.name) {
        RoomsScreen(
            navController = navController,
            roomsViewModel = roomsViewModel,
        )
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
}