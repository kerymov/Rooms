package com.example.rooms.presentation

sealed class UiState {
    data class Success<T : Any>(val data: T) : UiState()
    data class Error(val code: Int?, val message: String?) : UiState()
    data object Loading : UiState()
    data object None : UiState()
}