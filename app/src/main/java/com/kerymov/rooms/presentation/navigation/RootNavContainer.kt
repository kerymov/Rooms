package com.kerymov.rooms.presentation.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kerymov.rooms.R
import com.kerymov.rooms.presentation.components.BottomNavigationBar
import com.kerymov.rooms.presentation.components.BottomNavigationItem
import com.kerymov.ui_core.models.UserUi
import com.kerymov.ui_core.utils.LocalUser
import com.kerymov.ui_onboarding.screens.SignInScreen
import com.kerymov.ui_onboarding.screens.SignUpScreen
import com.kerymov.ui_onboarding.viewModels.AuthViewModel
import com.kerymov.ui_profile.screens.ProfileScreen
import com.kerymov.ui_profile.viewModels.ProfileViewModel
import com.kerymov.ui_room.models.RoomDetailsUi
import com.kerymov.ui_room.screens.RoomScreen
import com.kerymov.ui_room.viewModels.RoomViewModel
import com.kerymov.ui_rooms.screens.RoomsScreen
import com.kerymov.ui_rooms.viewModels.RoomsViewModel
import kotlinx.serialization.json.Json

@Composable
fun RootNavContainer(
    currentUser: UserUi?,
    startNavModule: NavModule,
    rootViewModel: RootViewModel
) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
            val shouldShowBottomNavigation = currentBackStackEntry?.destination?.hierarchy?.any {
                it.hasRoute(Main::class)
            } == true

            if (shouldShowBottomNavigation) {
                val bottomNavItems = listOf(
                    BottomNavigationItem(
                        title = "Rooms",
                        route = Main.Rooms,
                        selectedIcon = ImageVector.vectorResource(id = R.drawable.home_filled),
                        unselectedIcon = ImageVector.vectorResource(id = R.drawable.home),
                    ),
                    BottomNavigationItem(
                        title = "Profile",
                        route = Main.Profile,
                        selectedIcon = ImageVector.vectorResource(id = R.drawable.profile_filled),
                        unselectedIcon = ImageVector.vectorResource(id = R.drawable.profile),
                    ),
                )

                BottomNavigationBar(
                    items = bottomNavItems.map { item ->
                        item to (currentBackStackEntry?.destination?.hierarchy?.any {
                            it.hasRoute(item.route::class)
                        } == true)
                    },
                    onNavItemClick = { item ->
                        navController.navigate(route = item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = contentColorFor(MaterialTheme.colorScheme.background),
        contentWindowInsets = WindowInsets(0.dp),
        modifier = Modifier.fillMaxSize()
    ) { contentPadding ->
        NavHost(
            navController = navController,
            startDestination = startNavModule,
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            navigation<Auth>(
                startDestination = Auth.SignIn
            ) {
                composable<Auth.SignIn> { backStackEntry ->
                    val viewModel = hiltViewModel<AuthViewModel>()

                    SignInScreen(
                        onSignInSuccess = {
                            navController.navigate(Main) {
                                popUpTo(Auth) { inclusive = true }
                                launchSingleTop = true
                            }
                        },
                        onGoToSignUpClick = {
                            viewModel.resetUiState()
                            navController.navigate(Auth.SignUp) {
                                launchSingleTop = true
                            }
                        },
                        authViewModel = viewModel
                    )
                }
                composable<Auth.SignUp> { backStackEntry ->
                    val viewModel = hiltViewModel<AuthViewModel>()

                    SignUpScreen(
                        onSignUpSuccess = {
                            navController.navigate(Main) {
                                popUpTo(Auth) { inclusive = true }
                                launchSingleTop = true
                            }
                        },
                        onGoToSignInClick = {
                            viewModel.resetUiState()
                            navController.navigate(Auth.SignIn) {
                                popUpTo(0)
                                launchSingleTop = true
                            }
                        },
                        authViewModel = viewModel
                    )
                }
            }
            navigation<Main>(
                startDestination = Main.Rooms
            ) {
                composable<Main.Rooms> {
                    val roomsViewModel = hiltViewModel<RoomsViewModel>()

                    CompositionLocalProvider(LocalUser provides currentUser) {
                        RoomsScreen(
                            onRoomLogin = { detailsJson ->
                                navController.navigate(route = Room.RoomMain(detailsJson)) {
                                    launchSingleTop = true
                                }
                            },
                            roomsViewModel = roomsViewModel
                        )
                    }
                }
                composable<Main.Profile> {
                    val profileViewModel = hiltViewModel<ProfileViewModel>()

                    CompositionLocalProvider(LocalUser provides currentUser) {
                        ProfileScreen(
                            onSignOut = {
                                navController.navigate(Auth) {
                                    popUpTo(0)
                                }
                            },
                            viewModel = profileViewModel
                        )
                    }
                }
            }
            navigation<Room>(startDestination = Room.RoomMain()) {
                composable<Room.RoomMain> { backStackEntry ->
                    val room: Room.RoomMain = backStackEntry.toRoute()
                    val roomDetails = room.details?.let { Json.decodeFromString<RoomDetailsUi>(it) }

                    roomDetails?.let { details ->
                        val viewModel = hiltViewModel<RoomViewModel, RoomViewModel.Factory>(
                            creationCallback = { factory -> factory.create(roomDetails = roomDetails) }
                        )

                        CompositionLocalProvider(LocalUser provides currentUser) {
                            RoomScreen(
                                roomViewModel = viewModel,
                                onExit = {
                                    navController.navigate(Main.Rooms) {
                                        popUpTo(0)
                                    }
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}
