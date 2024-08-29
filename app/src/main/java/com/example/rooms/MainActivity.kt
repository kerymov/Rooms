package com.example.rooms

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.rooms.presentation.RoomsApp
import com.example.rooms.presentation.ui.theme.RoomsTheme
import com.example.rooms.presentation.ui.viewModels.RoomsViewModel
import com.example.rooms.presentation.ui.viewModels.SignInViewModel
import com.example.rooms.presentation.ui.viewModels.SignUpViewModel
import com.example.rooms.presentation.ui.viewModels.SplashViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RoomsTheme {
                val splashViewModel by viewModels<SplashViewModel> {
                    SplashViewModel.createFactory(context = this)
                }
                val signInViewModel by viewModels<SignInViewModel> {
                    SignInViewModel.createFactory(context = this)
                }
                val signUpViewModel by viewModels<SignUpViewModel> {
                    SignUpViewModel.createFactory(this)
                }
                val roomsViewModel = ViewModelProvider(this)[RoomsViewModel::class.java]

                RoomsApp(
                    splashViewModel = splashViewModel,
                    signInViewModel = signInViewModel,
                    signUpViewModel = signUpViewModel,
                    roomsViewModel = roomsViewModel,
                )
            }
        }
    }
}