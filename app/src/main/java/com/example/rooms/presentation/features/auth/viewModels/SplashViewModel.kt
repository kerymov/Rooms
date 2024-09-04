package com.example.rooms.presentation.features.auth.viewModels

import android.content.Context
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.rooms.data.dataSource.LocalAccountDataSource
import com.example.rooms.data.dataSource.RemoteAccountDataSource
import com.example.rooms.data.repository.AccountRepositoryImpl
import com.example.rooms.domain.model.BaseResult
import com.example.rooms.domain.useCases.auth.GetUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

enum class SplashUiState {
    AUTHORIZED,
    UNAUTHORIZED,
    NONE,
}

class SplashViewModel(
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {
    var uiState = mutableStateOf(SplashUiState.NONE)
        private set

    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch {
            val result = viewModelScope.async(Dispatchers.IO) {
                getUserUseCase.invoke().first()
            }

            uiState.value = when (result.await()) {
                is BaseResult.Success -> SplashUiState.AUTHORIZED
                else -> SplashUiState.UNAUTHORIZED
            }
        }
    }

    companion object {

        fun createFactory(context: Context) = viewModelFactory {
            initializer {
                val repository = AccountRepositoryImpl(
                    LocalAccountDataSource(context = context),
                    RemoteAccountDataSource(),
                )

                SplashViewModel(
                    GetUserUseCase(repository),
                )
            }
        }
    }
}

val LocalSplashState = compositionLocalOf<SplashViewModel> { error("User State Context Not Found!") }