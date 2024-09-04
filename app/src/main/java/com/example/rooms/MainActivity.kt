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
import com.example.rooms.presentation.ui.viewModels.LocalSplashState
import com.example.rooms.presentation.ui.viewModels.RoomsViewModel
import com.example.rooms.presentation.ui.viewModels.SplashUiState
import com.example.rooms.presentation.ui.viewModels.SplashViewModel
import com.example.rooms.presentation.ui.viewModels.AuthViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashViewModel by viewModels<SplashViewModel> {
            SplashViewModel.createFactory(context = this)
        }

        installSplashScreen().setKeepOnScreenCondition {
            splashViewModel.uiState.value == SplashUiState.NONE
        }

        setContent {
            RoomsTheme {
                val authViewModel by viewModels<AuthViewModel> {
                    AuthViewModel.createFactory(context = this)
                }
                val roomsViewModel = ViewModelProvider(this)[RoomsViewModel::class.java]

                CompositionLocalProvider(LocalSplashState provides splashViewModel) {
                    ApplicationManager(
                        authViewModel = authViewModel,
                        roomsViewModel = roomsViewModel,
                    )
                }
            }
        }
    }
}

@Composable
private fun ApplicationManager(
    roomsViewModel: RoomsViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val splashState = LocalSplashState.current

    val startNavContainer = when (splashState.uiState.value) {
        SplashUiState.AUTHORIZED -> NavSection.Rooms
        SplashUiState.UNAUTHORIZED -> NavSection.Auth
        else -> return
    }

    val navController = rememberNavController()
    RoomsApp(
        navController = navController,
        startNavSection = startNavContainer,
        roomsViewModel = roomsViewModel,
        authViewModel = authViewModel
    )
}