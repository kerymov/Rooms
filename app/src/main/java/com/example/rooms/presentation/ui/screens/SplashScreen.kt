package com.example.rooms.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

    var isLoading by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(uiState) {
        when (uiState) {
            is SplashUiState.Success -> {
                navController.navigate(Screen.ROOMS.name)
            }

            is SplashUiState.Error -> {
                navController.navigate(Screen.SIGN_IN.name)
            }

            is SplashUiState.Loading -> { isLoading = true }

            is SplashUiState.None -> { isLoading = false }
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
    ) {
        Logo()

        if (isLoading) {
            CircularProgressIndicator()
        }
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