package com.example.rooms.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rooms.data.models.Room
import com.example.rooms.data.models.UserLoginData
import com.example.rooms.data.network.RetrofitInstance
import com.example.rooms.presentation.navigation.Screen
import com.example.rooms.presentation.screens.RoomsScreen
import com.example.rooms.presentation.screens.SignInScreen
import com.example.rooms.presentation.screens.SignUpScreen
import com.example.rooms.presentation.theme.ChangeSystemBarsColors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun RoomsApp(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.SIGN_IN.name,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(route = Screen.SIGN_IN.name) {
            SignInScreen(
                onSignInClick = { login, password ->
                    CoroutineScope(Dispatchers.Default).launch {
                        val a = RetrofitInstance.accountApi.signIn(UserLoginData(login, password))
                    }
                }
            )
        }
        composable(route = Screen.SIGN_UP.name) {
            SignUpScreen()
        }
        composable(route = Screen.ROOMS.name) {

        }
        composable(
            route = Screen.ROOM.name + "/{roomId}",
            arguments = listOf(navArgument("roomId") { type = NavType.LongType })
        ) { backStackEntry ->

        }
        composable(route = Screen.RESULTS.name) {

        }
    }
}

@Preview
@Composable
private fun RoomsAppPreview() {
    RoomsApp()
}
