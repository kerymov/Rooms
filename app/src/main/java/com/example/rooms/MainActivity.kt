package com.example.rooms

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rooms.presentation.RoomsApp
import com.example.rooms.presentation.navigation.Screen
import com.example.rooms.presentation.theme.RoomsTheme
import com.example.rooms.presentation.viewModels.RoomViewModel
import com.example.rooms.presentation.viewModels.SignInViewModel
import com.example.rooms.presentation.viewModels.RoomsViewModel
import com.example.rooms.utils.AppPreferences

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppPreferences.setup(applicationContext)

        setContent {
            RoomsTheme {
                val signInViewModel = ViewModelProvider(this)[SignInViewModel::class.java]
                val roomsViewModel = ViewModelProvider(this)[RoomsViewModel::class.java]

                val token = AppPreferences.accessToken
                val startDestination = if (token == null) {
                    Screen.SIGN_IN.name
                } else {
                    Screen.ROOMS.name
                }

                RoomsApp(
                    signInViewModel = signInViewModel,
                    roomsViewModel = roomsViewModel,
                    startDestination = startDestination
                )
            }
        }
    }
}