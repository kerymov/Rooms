package com.example.rooms.presentation.navigation.navContainers

import com.example.rooms.domain.repository.AccountRepository
import com.example.rooms.presentation.features.auth.screens.SignInScreen
import com.example.rooms.presentation.features.auth.screens.SignUpScreen
import com.example.rooms.presentation.features.auth.viewModels.AuthViewModel
import com.example.rooms.presentation.features.utils.sharedViewModel
import com.example.rooms.presentation.navigation.NavModule
import com.example.rooms.presentation.navigation.navigate
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AuthNavContainer(
    onAuthSuccess: () -> Unit,
    accountRepository: AccountRepository
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
                factory = AuthViewModel.createFactory(accountRepository)
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
                factory = AuthViewModel.createFactory(accountRepository)
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
