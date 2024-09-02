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
import com.example.rooms.presentation.RoomsApp
import com.example.rooms.presentation.ui.navigation.Screen
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
                    val userState = LocalUserState.current
                    if (userState.uiState.isUserAuthorized) {
                        RoomsApp(
                            signInViewModel = signInViewModel,
                            signUpViewModel = signUpViewModel,
                            roomsViewModel = roomsViewModel,
                            startDestination = Screen.ROOMS
                        )
                    } else {
                        RoomsApp(
                            signInViewModel = signInViewModel,
                            signUpViewModel = signUpViewModel,
                            roomsViewModel = roomsViewModel,
                            startDestination = Screen.SIGN_IN
                        )
                    }
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

    if (userState.uiState.isUserAuthorized) {
        RoomsApp(
            signInViewModel = signInViewModel,
            signUpViewModel = signUpViewModel,
            roomsViewModel = roomsViewModel,
            startDestination = Screen.ROOMS
        )
    } else {
        RoomsApp(
            signInViewModel = signInViewModel,
            signUpViewModel = signUpViewModel,
            roomsViewModel = roomsViewModel,
            startDestination = Screen.SIGN_IN
        )
    }
}