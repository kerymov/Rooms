package com.example.rooms.presentation

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.rooms.presentation.features.auth.viewModels.LocalSplashState
import com.example.rooms.presentation.features.auth.viewModels.SplashUiState
import com.example.rooms.presentation.features.auth.viewModels.SplashViewModel
import com.example.rooms.presentation.navigation.NavModule
import com.example.rooms.presentation.theme.RoomsTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val splashViewModel by viewModels<SplashViewModel> {
            SplashViewModel.createFactory(context = this)
        }

        installSplashScreen().setKeepOnScreenCondition {
            splashViewModel.uiState.value == SplashUiState.NONE
        }

        setContent {
            RoomsTheme {
                CompositionLocalProvider(LocalSplashState provides splashViewModel) {
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
        SplashUiState.AUTHORIZED -> NavModule.Main
        SplashUiState.UNAUTHORIZED -> NavModule.Auth
        else -> return
    }

    RoomsApp(
        startNavModule = startNavModule,
    )
}