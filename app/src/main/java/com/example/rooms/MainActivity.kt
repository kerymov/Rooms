package com.example.rooms

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import com.example.rooms.presentation.RoomsApp
import com.example.rooms.presentation.ui.navigation.Screen
import com.example.rooms.presentation.ui.theme.RoomsTheme
import com.example.rooms.presentation.ui.viewModels.RoomsViewModel
import com.example.rooms.presentation.ui.viewModels.SignInViewModel
import com.example.rooms.presentation.ui.viewModels.SignUpViewModel
import com.example.rooms.presentation.ui.viewModels.SplashUiState
import com.example.rooms.presentation.ui.viewModels.SplashViewModel
import com.example.rooms.utils.AppPreferences

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppPreferences.setup(applicationContext)

        setContent {
            RoomsTheme {
                val splashViewModel by viewModels<SplashViewModel> {
                    SplashViewModel.createFactory(context = this)
                }
                val signInViewModel by viewModels<SignInViewModel> {
                    SignInViewModel.createFactory(context = this)
                }
                val signUpViewModel by viewModels<SignUpViewModel> {
                    SignUpViewModel.createFactory(this)
                }
                val roomsViewModel = ViewModelProvider(this)[RoomsViewModel::class.java]

                val splashUiState by splashViewModel.uiState.collectAsState()

                val startDestination = if (splashUiState is SplashUiState.Success) {
                    Screen.ROOMS.name
                } else {
                    Screen.SIGN_IN.name
                }

                RoomsApp(
                    signInViewModel = signInViewModel,
                    signUpViewModel = signUpViewModel,
                    roomsViewModel = roomsViewModel,
                    startDestination = startDestination,
                )
            }
        }
    }
}