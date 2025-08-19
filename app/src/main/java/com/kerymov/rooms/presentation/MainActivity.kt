package com.kerymov.rooms.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kerymov.rooms.presentation.splash.LocalSplashState
import com.kerymov.rooms.presentation.splash.SplashUiState
import com.kerymov.rooms.presentation.splash.SplashViewModel
import com.kerymov.rooms.presentation.navigation.Auth
import com.kerymov.rooms.presentation.navigation.Main
import com.kerymov.rooms.presentation.navigation.RootNavContainer
import com.kerymov.rooms.presentation.navigation.RootViewModel
import com.kerymov.ui_core.theme.RoomsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color.Transparent.toArgb(), Color.Transparent.toArgb()),
            navigationBarStyle = SystemBarStyle.auto(Color.Transparent.toArgb(), Color.Transparent.toArgb())
        )

        val splashViewModel by viewModels<SplashViewModel>()

        installSplashScreen().setKeepOnScreenCondition {
            splashViewModel.uiState.value == SplashUiState.None
        }

        setContent {
            RoomsTheme {
                CompositionLocalProvider(
                    LocalSplashState provides splashViewModel
                ) {
                    ApplicationManager()
                }
            }
        }
    }
}

@Composable
private fun ApplicationManager() {
    val splashState = LocalSplashState.current

    val startNavModule = when (splashState.uiState.value) {
        is SplashUiState.Authorized -> Main
        is SplashUiState.Unauthorized -> Auth
        else -> return
    }

    val rootViewModel = viewModel<RootViewModel>()

    RootNavContainer(
        currentUser = splashState.uiState.value.user,
        startNavModule = startNavModule,
        rootViewModel = rootViewModel
    )
}