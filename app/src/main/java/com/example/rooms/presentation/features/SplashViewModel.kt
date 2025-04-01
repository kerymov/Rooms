package com.example.rooms.presentation.features

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

sealed class SplashUiState {
    data class Authorized(val user: String) : SplashUiState()
    object Unauthorized : SplashUiState()
    object None : SplashUiState()
}

class SplashViewModel(
//    private val getUserUseCase: GetUserUseCase
) : ViewModel() {
    var uiState = mutableStateOf<SplashUiState>(SplashUiState.None)
        private set

    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch {
//            val user = viewModelScope.async(Dispatchers.IO) {
//                getUserUseCase.invoke()
//            }
//
//            uiState.value = user.await()?.let {
//                val userUiModel = it.mapToUiModel()
//                SplashUiState.Authorized(userUiModel)
//            } ?: SplashUiState.Unauthorized
            uiState.value = SplashUiState.Unauthorized
        }
    }

//    companion object {
//
//        fun createFactory(repository: AccountRepository) = viewModelFactory {
//            initializer {
//                SplashViewModel(
//                    GetUserUseCase(repository),
//                )
//            }
//        }
//    }
}

val LocalSplashState =
    compositionLocalOf<SplashViewModel> { error("User State Context Not Found!") }