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
import com.example.rooms.domain.repository.AccountRepository
import com.example.rooms.domain.repository.RoomRepository
import com.example.rooms.domain.repository.RoomsRepository
import com.example.rooms.presentation.features.auth.viewModels.LocalSplashState
import com.example.rooms.presentation.features.auth.viewModels.SplashUiState
import com.example.rooms.presentation.features.auth.viewModels.SplashViewModel
import com.example.rooms.presentation.navigation.Auth
import com.example.rooms.presentation.navigation.Main
import com.example.rooms.presentation.navigation.RootNavContainer
import com.example.rooms.presentation.navigation.RootViewModel
import com.example.rooms.presentation.theme.RoomsTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    lateinit var accountRepository: AccountRepository
    lateinit var roomsRepository: RoomsRepository
    lateinit var roomRepository: RoomRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        accountRepository = (application as RoomsApp).accountRepository
        roomsRepository = (application as RoomsApp).roomsRepository
        roomRepository = (application as RoomsApp).roomRepository

        val splashViewModel by viewModels<SplashViewModel> {
            SplashViewModel.createFactory(accountRepository)
        }

        installSplashScreen().setKeepOnScreenCondition {
            splashViewModel.uiState.value == SplashUiState.None
        }

        setContent {
            RoomsTheme {
                CompositionLocalProvider(LocalSplashState provides splashViewModel) {
                    ApplicationManager(
                        accountRepository = accountRepository,
                        roomsRepository = roomsRepository,
                        roomRepository = roomRepository
                    )
                }
            }
        }
    }
}

@Composable
private fun ApplicationManager(
    accountRepository: AccountRepository,
    roomsRepository: RoomsRepository,
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
        accountRepository = accountRepository,
        roomsRepository = roomsRepository,
        roomRepository = roomRepository
    )
}