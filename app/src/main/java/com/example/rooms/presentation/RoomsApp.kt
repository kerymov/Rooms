package com.example.rooms.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.rooms.presentation.ui.navigation.AppNavHost
import com.example.rooms.presentation.ui.navigation.Screen
import com.example.rooms.presentation.ui.viewModels.RoomsViewModel
import com.example.rooms.presentation.ui.viewModels.SignInViewModel
import com.example.rooms.presentation.ui.viewModels.SignUpViewModel

@Composable
fun RoomsApp(
    signInViewModel: SignInViewModel = viewModel(),
    signUpViewModel: SignUpViewModel = viewModel(),
    roomsViewModel: RoomsViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
    startDestination: Screen,
) = Scaffold(
//    bottomBar = {
//        BottomNavigation(
//            backgroundColor = MaterialTheme.colors.primary
//        ) {
//            BottomNavigationItem(
//                selected = true,
//                onClick = { navController.navigate(Screen.ROOMS.name) },
//                icon = {
//                    Icon(
//                        imageVector = Icons.Filled.Home,
//                        contentDescription = null,
//                        tint = MaterialTheme.colors.onPrimary
//                    )
//                }
//            )
//
//            BottomNavigationItem(
//                selected = false,
//                onClick = { navController.navigate(Screen.SIGN_IN.name) },
//                icon = {
//                    Icon(
//                        imageVector = Icons.Filled.Settings,
//                        contentDescription = null,
//                        tint = MaterialTheme.colors.onPrimary
//                    )
//                }
//            )
//        }
//    }
) { contentPadding ->
    AppNavHost(
        navController = navController,
        startDestination = startDestination,
        signInViewModel = signInViewModel,
        signUpViewModel = signUpViewModel,
        roomsViewModel = roomsViewModel,
        modifier = Modifier.padding(contentPadding)
    )
}

@Preview
@Composable
private fun RoomsAppPreview() {
//    RoomsApp(listOf())
}
