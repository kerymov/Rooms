package com.example.rooms.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.rooms.presentation.components.BottomNavigationBar
import com.example.rooms.presentation.components.BottomNavigationItem
import com.example.rooms.presentation.features.auth.viewModels.AuthViewModel
import com.example.rooms.presentation.features.rooms.screens.RoomsScreen
import com.example.rooms.presentation.features.rooms.viewModels.RoomsViewModel
import com.example.rooms.presentation.features.utils.sharedViewModel

@Composable
fun MainNavModule() {
    val navController = rememberNavController()
    val startDestination = NavModule.Main.Rooms

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                items = BottomNavigationItem.entries,
                onNavItemClick = { item ->
                    navController.navigate(submodule = item.destination, param = null)
                }
            )
        }
    ) { contentPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            navigation(
                route = NavModule.Main.Rooms.route,
                startDestination = NavModule.Main.Rooms.Rooms.route
            ) {
                composable(route = NavModule.Main.Rooms.Rooms.route) { backStackEntry ->
                    val viewModel = backStackEntry.sharedViewModel<RoomsViewModel>(
                        navController = navController
                    )

                    RoomsScreen(
                        navController = navController,
                        roomsViewModel = viewModel
                    )
                }
                composable(route = NavModule.Main.Rooms.Room.route) {

                }
                composable(route = NavModule.Main.Rooms.Results.route) {

                }
            }
            navigation(
                route = NavModule.Main.Profile.route,
                startDestination = NavModule.Main.Profile.Profile.route
            ) {
                composable(route = NavModule.Main.Profile.Profile.route) {
                    Text(text = "Profile")
                }
            }
        }
    }
}