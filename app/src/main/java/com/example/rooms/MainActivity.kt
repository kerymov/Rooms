package com.example.rooms

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.rooms.presentation.RoomsApp
import com.example.rooms.presentation.theme.RoomsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RoomsTheme {
                RoomsApp()
            }
        }
    }
}