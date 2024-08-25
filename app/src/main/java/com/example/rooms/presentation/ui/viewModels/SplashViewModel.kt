package com.example.rooms.presentation.ui.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.rooms.data.dataSource.LocalAccountDataSource
import com.example.rooms.data.dataSource.RemoteAccountDataSource
import com.example.rooms.data.repository.AccountRepositoryImpl
import com.example.rooms.domain.model.BaseResult
import com.example.rooms.domain.model.User
import com.example.rooms.domain.useCases.GetUserUseCase
import com.example.rooms.domain.useCases.SignUpUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

sealed class SplashUiState {
    data object Success : SplashUiState()
    data class Error(val code: Int?, val message: String?) : SplashUiState()
    data object Loading : SplashUiState()
    data object None : SplashUiState()
}

class SplashViewModel(
    private val getUserUseCase: GetUserUseCase
): ViewModel() {

    private val _uiState: MutableStateFlow<SplashUiState> = MutableStateFlow(SplashUiState.None)
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    init {
        getUser()
    }

    private fun getUser() {
        _uiState.value = SplashUiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            getUserUseCase.invoke()
                .catch { e ->
                    _uiState.value = SplashUiState.Error(code = null, message = e.localizedMessage)
                }
                .collect { result ->
                    _uiState.value = when (result) {
                        is BaseResult.Success -> { SplashUiState.Success }
                        is BaseResult.Error -> SplashUiState.Error(result.code, result.message)
                        is BaseResult.Exception -> SplashUiState.Error(null, result.message)
                    }
                }
        }
    }

    companion object {

        fun createFactory(context: Context) = viewModelFactory {
            initializer {
                SplashViewModel(
                    GetUserUseCase(
                        AccountRepositoryImpl(
                            LocalAccountDataSource(context = context),
                            RemoteAccountDataSource(),
                        )
                    )
                )
            }
        }
    }
}