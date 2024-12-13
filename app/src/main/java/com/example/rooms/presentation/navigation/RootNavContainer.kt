package com.example.rooms.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.rooms.domain.repository.AccountRepository
import com.example.rooms.domain.repository.RoomRepository
import com.example.rooms.domain.repository.RoomsRepository
import com.example.rooms.presentation.components.BottomNavigationBar
import com.example.rooms.presentation.components.BottomNavigationItem
import com.example.rooms.presentation.features.auth.screens.SignInScreen
import com.example.rooms.presentation.features.auth.screens.SignUpScreen
import com.example.rooms.presentation.features.auth.viewModels.AuthViewModel
import com.example.rooms.presentation.features.main.profile.screens.ProfileScreen
import com.example.rooms.presentation.features.main.profile.viewModels.ProfileViewModel
import com.example.rooms.presentation.features.main.rooms.models.RoomDetailsUi
import com.example.rooms.presentation.features.room.screens.ResultsScreen
import com.example.rooms.presentation.features.room.screens.RoomScreen
import com.example.rooms.presentation.features.main.rooms.screens.RoomsScreen
import com.example.rooms.presentation.features.room.viewModels.RoomViewModel
import com.example.rooms.presentation.features.main.rooms.viewModels.RoomsViewModel
import com.example.rooms.presentation.features.utils.sharedViewModel
import com.example.rooms.presentation.features.utils.toOuterScaffoldPadding
import com.example.rooms.presentation.navigation.Main.Rooms
import kotlinx.serialization.json.Json

@Composable
fun RootNavContainer(
    startNavModule: NavModule,
    accountRepository: AccountRepository,
    roomsRepository: RoomsRepository,
    roomRepository: RoomRepository,
) {
    val navController = rememberNavController()
    var currentNavModule by remember { mutableStateOf(startNavModule) }

    Scaffold(
        bottomBar = {
            if (currentNavModule == Main) {
                BottomNavigationBar(
                    items = BottomNavigationItem.entries,
                    onNavItemClick = { item ->
                        navController.navigate(route = item.destination)
                    }
                )
            }
        },
    ) { contentPadding ->
        NavHost(
            navController = navController,
            startDestination = startNavModule,
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding.toOuterScaffoldPadding())
        ) {
            navigation<Auth>(
                startDestination = Auth.SignIn
            ) {
                composable<Auth.SignIn> { backStackEntry ->
                    currentNavModule = Auth

                    val viewModel = backStackEntry.sharedViewModel<AuthViewModel>(
                        navController = navController,
                        factory = AuthViewModel.createFactory(accountRepository)
                    )

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
                    currentNavModule = Auth

                    val viewModel = backStackEntry.sharedViewModel<AuthViewModel>(
                        navController = navController,
                        factory = AuthViewModel.createFactory(accountRepository)
                    )

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
                startDestination = Rooms
            ) {
                composable<Rooms> {
                    currentNavModule = Main

                    val roomsViewModel = viewModel<RoomsViewModel>(
                        factory = RoomsViewModel.createFactory(roomsRepository)
                    )

                    RoomsScreen(
                        onRoomLogin = { detailsJson ->
                            navController.navigate(
                                route = Room.RoomMain(detailsJson)
                            )
                        },
                        roomsViewModel = roomsViewModel
                    )
                }
                composable<Main.Profile> {
                    currentNavModule = Main

                    val profileViewModel = viewModel<ProfileViewModel>(
                        factory = ProfileViewModel.createFactory(accountRepository)
                    )

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
            navigation<Room>(startDestination = Room.RoomMain()) {
                composable<Room.RoomMain> { backStackEntry ->
                    currentNavModule = Room

                    val room: Room.RoomMain = backStackEntry.toRoute()
                    val roomDetails = room.details?.let { Json.decodeFromString<RoomDetailsUi>(it) }

                    roomDetails?.let { details ->
                        val viewModel = backStackEntry.sharedViewModel<RoomViewModel>(
                            navController = navController,
                            factory = RoomViewModel.createFactory(details, roomRepository)
                        )
                        RoomScreen(
                            onNavigateBack = {
                                navController.navigate(Rooms) {
                                    popUpTo(0)
                                }
                            },
                            onNavigateToResults = {
                                navController.navigate(Room.Results) {

                                }
                            },
                            roomViewModel = viewModel,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                composable<Room.Results> { backStackEntry ->
                    currentNavModule = Room

                    val viewModel = backStackEntry.sharedViewModel<RoomViewModel>(
                        navController = navController
                    )

                    ResultsScreen(
                        navController = navController,
                        roomName = ""
                    )
                }
            }
        }
    }
}
