package com.example.rooms.presentation.splash

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain_core.auth.AuthTokenProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SplashUiState {
    data object Authorized : SplashUiState()
    data object Unauthorized : SplashUiState()
    data object None : SplashUiState()
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authTokenProvider: AuthTokenProvider
) : ViewModel() {
    var uiState = mutableStateOf<SplashUiState>(SplashUiState.None)
        private set

    init {
        getAuthToken()
    }

    private fun getAuthToken() {
        viewModelScope.launch {
            authTokenProvider.authToken.collect { token ->
                uiState.value =
                    token?.let { SplashUiState.Authorized } ?: SplashUiState.Unauthorized
            }
        }
    }
}

val LocalSplashState =
    compositionLocalOf<SplashViewModel> { error("User State Context Not Found!") }