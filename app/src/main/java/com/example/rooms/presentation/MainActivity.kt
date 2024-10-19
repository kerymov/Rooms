package com.example.rooms.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.rooms.common.RoomsApp
import com.example.rooms.domain.repository.AccountRepository
import com.example.rooms.domain.repository.RoomsRepository
import com.example.rooms.presentation.features.auth.viewModels.LocalSplashState
import com.example.rooms.presentation.features.auth.viewModels.SplashUiState
import com.example.rooms.presentation.features.auth.viewModels.SplashViewModel
import com.example.rooms.presentation.navigation.NavModule
import com.example.rooms.presentation.navigation.navContainers.RootNavContainer
import com.example.rooms.presentation.theme.RoomsTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val accountRepository = (application as RoomsApp).accountRepository
        val roomsRepository = (application as RoomsApp).roomsRepository

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
                        roomsRepository = roomsRepository
                    )
                }
            }
        }
    }
}

@Composable
private fun ApplicationManager(
    accountRepository: AccountRepository,
    roomsRepository: RoomsRepository
) {
    val splashState = LocalSplashState.current

    val startNavModule = when (splashState.uiState.value) {
        is SplashUiState.Authorized -> NavModule.Main
        is SplashUiState.Unauthorized -> NavModule.Auth
        else -> return
    }

    RootNavContainer(
        startNavModule = startNavModule,
        accountRepository = accountRepository,
        roomsRepository = roomsRepository
    )
}