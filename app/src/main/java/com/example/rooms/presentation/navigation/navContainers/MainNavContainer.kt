package com.example.rooms.presentation.navigation.navContainers

import com.example.rooms.domain.repository.AccountRepository
import com.example.rooms.presentation.components.BottomNavigationBar
import com.example.rooms.presentation.components.BottomNavigationItem
import com.example.rooms.presentation.features.main.profile.screens.ProfileScreen
import com.example.rooms.presentation.features.main.profile.viewModels.ProfileViewModel
import com.example.rooms.presentation.features.utils.sharedViewModel
import com.example.rooms.presentation.features.utils.toOuterScaffoldPadding
import com.example.rooms.presentation.navigation.NavModule
import com.example.rooms.presentation.navigation.navigate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.rooms.domain.repository.RoomsRepository
import com.example.rooms.presentation.features.main.rooms.screens.RoomsScreen
import com.example.rooms.presentation.features.main.rooms.viewModels.RoomsViewModel

@Composable
fun MainNavContainer(
    parentNavController: NavHostController,
    accountRepository: AccountRepository,
    roomsRepository: RoomsRepository,
) {
    val navController = rememberNavController()
    val startDestination = NavModule.Main.Rooms

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                items = BottomNavigationItem.entries,
                onNavItemClick = { item ->
                    navController.navigate(route = item.destination.route)
                }
            )
        },
    ) { contentPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding.toOuterScaffoldPadding())
        ) {
            navigation(
                route = NavModule.Main.Rooms.route,
                startDestination = NavModule.Main.Rooms.Rooms.route
            ) {
                composable(route = NavModule.Main.Rooms.Rooms.route) { backStackEntry ->
                    val viewModel = backStackEntry.sharedViewModel<RoomsViewModel>(
                        navController = navController,
                        factory = RoomsViewModel.createFactory(roomsRepository)
                    )

                    RoomsScreen(
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
                composable(route = NavModule.Main.Profile.Profile.route) { backStackEntry ->
                    val viewModel = backStackEntry.sharedViewModel<ProfileViewModel>(
                        navController = navController,
                        factory = ProfileViewModel.createFactory(accountRepository)
                    )

                    ProfileScreen(
                        onSignOut = {
                            parentNavController.navigate(NavModule.Auth, param = null) {
                                popUpTo(0)
                            }
                        },
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}