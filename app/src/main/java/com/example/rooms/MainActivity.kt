package com.example.rooms

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.rooms.presentation.RoomsApp
import com.example.rooms.presentation.ui.navigation.Screen
import com.example.rooms.presentation.ui.theme.RoomsTheme
import com.example.rooms.presentation.ui.viewModels.RoomsViewModel
import com.example.rooms.presentation.ui.viewModels.SignInViewModel
import com.example.rooms.presentation.ui.viewModels.SignUpViewModel
import com.example.rooms.utils.AppPreferences

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppPreferences.setup(applicationContext)

        setContent {
            RoomsTheme {
                val signInViewModel by viewModels<SignInViewModel> { SignInViewModel.Factory  }
                val signUpViewModel by viewModels<SignUpViewModel> { SignUpViewModel.Factory  }
                val roomsViewModel = ViewModelProvider(this)[RoomsViewModel::class.java]

//                val token = AppPreferences.accessToken
//                val startDestination = if (token == null) {
//                    Screen.SIGN_IN.name
//                } else {
//                    Screen.ROOMS.name
//                }

                RoomsApp(
                    signInViewModel = signInViewModel,
                    signUpViewModel = signUpViewModel,
                    roomsViewModel = roomsViewModel,
                    startDestination = Screen.SIGN_IN.name
                )
            }
        }
    }
}