package com.example.rooms.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.rooms.presentation.ui.components.Logo
import com.example.rooms.presentation.ui.navigation.Screen
import com.example.rooms.presentation.ui.viewModels.SplashUiState
import com.example.rooms.presentation.ui.viewModels.SplashViewModel

@Composable
fun SplashScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    splashViewModel: SplashViewModel = viewModel()
) {
    val uiState by splashViewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        when (uiState) {
            is SplashUiState.Success -> {
                navController.navigate(Screen.SIGN_IN.name)
            }

            is SplashUiState.Error -> {
                navController.navigate(Screen.SIGN_IN.name)
            }

            is SplashUiState.Loading -> { }

            is SplashUiState.None -> { }
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Logo()
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    SplashScreen(
        navController = rememberNavController(),
        modifier = Modifier.fillMaxSize(),
    )
}