package com.example.rooms.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rooms.presentation.navigation.AuthNavModule
import com.example.rooms.presentation.navigation.MainNavModule
import com.example.rooms.presentation.navigation.NavModule
import com.example.rooms.presentation.navigation.navigate

@Composable
fun RoomsApp(
    startNavModule: NavModule
) {
    val navController = rememberNavController()

    Surface {
        NavHost(
            navController = navController,
            startDestination = startNavModule.route,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(route = NavModule.Auth.route) {
                AuthNavModule(
                    onAuthSuccess = { navController.navigateToMainModule() },
                )
            }
            composable(route = NavModule.Main.route) {
                MainNavModule(navController)
            }
        }
    }
}

private fun NavHostController.navigateToMainModule() {
    navigate(module = NavModule.Main, param = null) {
        popUpTo(NavModule.Auth.route) {
            inclusive = true
        }
    }
}
