package com.example.rooms

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.rooms.presentation.RoomsApp
import com.example.rooms.presentation.theme.RoomsTheme
import com.example.rooms.presentation.viewModels.SignInViewModel
import com.example.rooms.presentation.viewModels.RoomsViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RoomsTheme {
                val signInViewModel = ViewModelProvider(this)[SignInViewModel::class.java]
                val roomsViewModel = ViewModelProvider(this)[RoomsViewModel::class.java]

                RoomsApp(
                    signInViewModel = signInViewModel,
                    roomsViewModel = roomsViewModel
                )
            }
        }
    }
}