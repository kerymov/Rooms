package com.example.rooms.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.rooms.R
import com.example.rooms.presentation.components.BottomNavigationBar
import com.example.rooms.presentation.components.CenterAlignedTopBar
import com.example.rooms.presentation.components.TopAppBarInteractionItem
import com.example.rooms.presentation.components.TopAppBarItem
import com.example.ui_core.models.UserUi
import com.example.ui_core.utils.LocalUser
import com.example.ui_onboarding.screens.SignInScreen
import com.example.ui_onboarding.screens.SignUpScreen
import com.example.ui_onboarding.viewModels.AuthViewModel
import com.example.ui_profile.screens.ProfileScreen
import com.example.ui_profile.viewModels.ProfileViewModel
import com.example.ui_room.models.RoomDetailsUi
import com.example.ui_room.screens.RoomScreen
import com.example.ui_room.viewModels.RoomViewModel
import com.example.ui_rooms.screens.RoomsScreen
import com.example.ui_rooms.viewModels.RoomsViewModel
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RootNavContainer(
    currentUser: UserUi?,
    startNavModule: NavModule,
    rootViewModel: RootViewModel
) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    val scaffoldState by rootViewModel.scaffoldState.collectAsState()
    val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        bottomBar = {
            val shouldShowBottomNavigation = currentBackStackEntry?.destination?.hierarchy?.any {
                it.hasRoute(Main::class)
            } == true

            if (shouldShowBottomNavigation) {
                val bottomNavItems = BottomNavigationItem.entries

                BottomNavigationBar(
                    items = bottomNavItems,
                    onNavItemClick = { item ->
                        navController.navigate(route = item.route)
                    }
                )
            }
        },
        topBar = {
            scaffoldState.topAppBar?.let {
                CenterAlignedTopBar(
                    item = it,
                    scrollBehaviour = topAppBarScrollBehavior
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = contentColorFor(MaterialTheme.colorScheme.background),
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
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
                    rootViewModel.updateTopAppBar(null)

                    val viewModel = hiltViewModel<AuthViewModel>()

                    SignInScreen(
                        onSignInSuccess = { navController.navigate(Main) },
                        onGoToSignUpClick = {
                            viewModel.resetUiState()
                            navController.navigate(Auth.SignUp)
                        },
                        authViewModel = viewModel
                    )
                }
                composable<Auth.SignUp> { backStackEntry ->
                    rootViewModel.updateTopAppBar(null)

                    val viewModel = hiltViewModel<AuthViewModel>()

                    SignUpScreen(
                        onSignUpSuccess = { navController.navigate(Main) },
                        onGoToSignInClick = {
                            viewModel.resetUiState()
                            navController.navigate(Auth.SignIn)
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

                    rootViewModel.updateTopAppBar(
                        item = TopAppBarItem(
                            title = stringResource(R.string.rooms),
                            actions = listOf(
                                TopAppBarInteractionItem(
                                    icon = Icons.Filled.Add,
                                    onClick = { roomsViewModel.toggleCreateRoomBottomSheet(isOpen = true) }
                                )
                            )
                        )
                    )

                    CompositionLocalProvider(LocalUser provides currentUser) {
                        RoomsScreen(
                            onRoomLogin = { detailsJson ->
                                navController.navigate(
                                    route = Room.RoomMain(detailsJson)
                                )
                            },
                            roomsViewModel = roomsViewModel
                        )
                    }
                }
                composable<Main.Profile> {
                    val profileViewModel = hiltViewModel<ProfileViewModel>()

                    rootViewModel.updateTopAppBar(
                        item = TopAppBarItem(
                            title = stringResource(R.string.profile)
                        )
                    )

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

                        rootViewModel.updateTopAppBar(
                            item = TopAppBarItem(
                                title = "${details.name} - ${details.settings.event.shortName}",
                                navigationItem = TopAppBarInteractionItem(
                                    icon = Icons.AutoMirrored.Filled.ExitToApp,
                                    onClick = {
                                        navController.navigate(Main.Rooms) {
                                            popUpTo(0)
                                        }
                                    }
                                ),
                                actions = listOf(
                                    TopAppBarInteractionItem(
                                        icon = Icons.Filled.Groups,
                                        onClick = { }
                                    )
                                ),
                            )
                        )

                        CompositionLocalProvider(LocalUser provides currentUser) {
                            RoomScreen(
                                roomViewModel = viewModel,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}
