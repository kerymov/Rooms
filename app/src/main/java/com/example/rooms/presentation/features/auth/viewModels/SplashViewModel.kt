package com.example.rooms.presentation.features.auth.viewModels

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.rooms.domain.repository.AccountRepository
import com.example.rooms.domain.useCases.auth.GetUserUseCase
import com.example.rooms.presentation.features.auth.models.UserUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

sealed class SplashUiState {
    data class Authorized(val user: UserUiModel) : SplashUiState()
    object Unauthorized : SplashUiState()
    object None : SplashUiState()
}

class SplashViewModel(
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {
    var uiState = mutableStateOf<SplashUiState>(SplashUiState.None)
        private set

    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch {
            val user = viewModelScope.async(Dispatchers.IO) {
                getUserUseCase.invoke()
            }

            uiState.value = user.await()?.let {
                val userUiModel = UserUiModel(
                    name = it.username,
                    token = it.token,
                    expiresIn = it.expiresIn
                )
                SplashUiState.Authorized(userUiModel)
            } ?: SplashUiState.Unauthorized
        }
    }

    companion object {

        fun createFactory(repository: AccountRepository) = viewModelFactory {
            initializer {
                SplashViewModel(
                    GetUserUseCase(repository),
                )
            }
        }
    }
}

val LocalSplashState =
    compositionLocalOf<SplashViewModel> { error("User State Context Not Found!") }