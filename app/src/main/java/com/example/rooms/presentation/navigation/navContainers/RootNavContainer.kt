package com.example.rooms.presentation.navigation.navContainers

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rooms.domain.repository.AccountRepository
import com.example.rooms.domain.repository.RoomsRepository
import com.example.rooms.presentation.navigation.NavModule
import com.example.rooms.presentation.navigation.navigate

@Composable
fun RootNavContainer(
    startNavModule: NavModule,
    accountRepository: AccountRepository,
    roomsRepository: RoomsRepository,
) {
    val navController = rememberNavController()

    Surface {
        NavHost(
            navController = navController,
            startDestination = startNavModule.route,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(route = NavModule.Auth.route) {
                AuthNavContainer(
                    onAuthSuccess = { navController.navigateToMainModule() },
                    accountRepository = accountRepository
                )
            }
            composable(route = NavModule.Main.route) {
                MainNavContainer(
                    parentNavController = navController,
                    accountRepository = accountRepository,
                    roomsRepository = roomsRepository
                )
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
