package com.example.rooms.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rooms.presentation.features.auth.screens.SignInScreen
import com.example.rooms.presentation.features.auth.screens.SignUpScreen
import com.example.rooms.presentation.features.auth.viewModels.AuthViewModel
import com.example.rooms.presentation.features.utils.sharedViewModel

@Composable
fun AuthNavModule(
    onAuthSuccess: () -> Unit,
) = Scaffold { contentPadding ->
    val navController = rememberNavController()
    val startDestination = NavModule.Auth.SignIn

    NavHost(
        navController = navController,
        startDestination = startDestination.route,
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        composable(route = NavModule.Auth.SignIn.route) { backStackEntry ->
            val viewModel = backStackEntry.sharedViewModel<AuthViewModel>(
                navController = navController,
                factory = AuthViewModel.createFactory(LocalContext.current)
            )

            SignInScreen(
                onSignInSuccess = onAuthSuccess,
                onGoToSignUpClick = {
                    viewModel.resetUiState()
                    navController.navigateTo(NavModule.Auth.SignUp)
                },
                authViewModel = viewModel
            )
        }
        composable(route = NavModule.Auth.SignUp.route) { backStackEntry ->
            val viewModel = backStackEntry.sharedViewModel<AuthViewModel>(
                navController = navController,
                factory = AuthViewModel.createFactory(LocalContext.current)
            )

            SignUpScreen(
                onSignUpSuccess = onAuthSuccess,
                onGoToSignInClick = {
                    viewModel.resetUiState()
                    navController.navigateTo(NavModule.Auth.SignIn)
                },
                authViewModel = viewModel
            )
        }
    }
}


private fun NavHostController.navigateTo(screen: NavModule.Screen) {
    navigate(screen = screen, param = null)
}
