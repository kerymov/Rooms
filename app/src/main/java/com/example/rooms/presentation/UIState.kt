package com.example.rooms.presentation

sealed class UIState {
    data class Success(val a: String): UIState()
    data class Error(val message: String): UIState()
    data object Loading: UIState()
}