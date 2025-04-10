package com.example.rooms.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rooms.common.RoomsApp
import com.example.rooms.domain.repository.RoomRepository
import com.example.rooms.presentation.features.LocalSplashState
import com.example.rooms.presentation.features.SplashUiState
import com.example.rooms.presentation.features.SplashViewModel
import com.example.rooms.presentation.navigation.Auth
import com.example.rooms.presentation.navigation.Main
import com.example.rooms.presentation.navigation.RootNavContainer
import com.example.rooms.presentation.navigation.RootViewModel
import com.example.rooms.presentation.theme.RoomsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    lateinit var roomRepository: RoomRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        roomRepository = (application as RoomsApp).roomRepository

        val splashViewModel by viewModels<SplashViewModel>()

        installSplashScreen().setKeepOnScreenCondition {
            splashViewModel.uiState.value == SplashUiState.None
        }

        setContent {
            RoomsTheme {
                CompositionLocalProvider(LocalSplashState provides splashViewModel) {
                    ApplicationManager(
                        roomRepository = roomRepository
                    )
                }
            }
        }
    }
}

@Composable
private fun ApplicationManager(
    roomRepository: RoomRepository,
) {
    val splashState = LocalSplashState.current

    val startNavModule = when (splashState.uiState.value) {
        is SplashUiState.Authorized -> Main
        is SplashUiState.Unauthorized -> Auth
        else -> return
    }

    val rootViewModel = viewModel<RootViewModel>()

    RootNavContainer(
        startNavModule = startNavModule,
        rootViewModel = rootViewModel,
        roomRepository = roomRepository
    )
}