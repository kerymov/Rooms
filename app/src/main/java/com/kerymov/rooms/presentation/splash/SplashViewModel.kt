package com.kerymov.rooms.presentation.splash

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerymov.domain_core.user.UserProvider
import com.kerymov.ui_core.models.UserUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SplashUiState(open val user: UserUi? = null) {
    data class Authorized(override val user: UserUi) : SplashUiState(user)
    data object Unauthorized : SplashUiState()
    data object None : SplashUiState()
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userProvider: UserProvider
) : ViewModel() {
    var uiState = mutableStateOf<SplashUiState>(SplashUiState.None)
        private set

    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch {
            userProvider.username.collect { username ->
                uiState.value =
                    username?.let {
                        SplashUiState.Authorized(UserUi(it))
                    } ?: SplashUiState.Unauthorized
            }
        }
    }
}

val LocalSplashState =
    compositionLocalOf<SplashViewModel> { error("User State Context Not Found!") }