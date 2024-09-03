package com.example.rooms

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.rooms.presentation.RoomsApp
import com.example.rooms.presentation.ui.navigation.NavSection
import com.example.rooms.presentation.ui.theme.RoomsTheme
import com.example.rooms.presentation.ui.viewModels.LocalUserState
import com.example.rooms.presentation.ui.viewModels.RoomsViewModel
import com.example.rooms.presentation.ui.viewModels.SignInViewModel
import com.example.rooms.presentation.ui.viewModels.SignUpViewModel
import com.example.rooms.presentation.ui.viewModels.UserViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userViewModel by viewModels<UserViewModel> {
            UserViewModel.createFactory(context = this)
        }

        installSplashScreen().setKeepOnScreenCondition {
            userViewModel.uiState.isLoading
        }

        setContent {
            RoomsTheme {
                val signInViewModel by viewModels<SignInViewModel> {
                    SignInViewModel.createFactory(context = this)
                }
                val signUpViewModel by viewModels<SignUpViewModel> {
                    SignUpViewModel.createFactory(this)
                }
                val roomsViewModel = ViewModelProvider(this)[RoomsViewModel::class.java]

                CompositionLocalProvider(LocalUserState provides userViewModel) {
                    ApplicationManager(
                        signInViewModel = signInViewModel,
                        signUpViewModel = signUpViewModel,
                        roomsViewModel = roomsViewModel,
                    )
                }
            }
        }
    }
}

@Composable
private fun ApplicationManager(
    signInViewModel: SignInViewModel = viewModel(),
    signUpViewModel: SignUpViewModel = viewModel(),
    roomsViewModel: RoomsViewModel = viewModel()
) {
    val userState = LocalUserState.current

    if (userState.uiState.isLoading) return

    val startNavContainer = if (userState.uiState.isUserAuthorized) {
        NavSection.Rooms
    } else {
        NavSection.Auth
    }

    val navController = rememberNavController()
    RoomsApp(
        navController = navController,
        startNavSection = startNavContainer,
        signInViewModel = signInViewModel,
        signUpViewModel = signUpViewModel,
        roomsViewModel = roomsViewModel,
    )
}