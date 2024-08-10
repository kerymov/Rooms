package com.example.rooms.presentation.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.rooms.data.dataSource.RemoteAccountDataSource
import com.example.rooms.data.repository.AccountRepositoryImpl
import com.example.rooms.domain.model.BaseResult
import com.example.rooms.domain.useCases.SignInUseCase
import com.example.rooms.presentation.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class SignInUiState {
    data object Success : SignInUiState()
    data class Error(val code: Int?, val message: String?) : SignInUiState()
    data object Loading : SignInUiState()
    data object None : SignInUiState()
}

class SignInViewModel(
    private val singInUseCase: SignInUseCase
) : ViewModel() {
    private val _uiState: MutableStateFlow<SignInUiState> = MutableStateFlow(SignInUiState.None)
    val uiState: StateFlow<SignInUiState>
        get() = _uiState.asStateFlow()

    fun signIn(
        login: String,
        password: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            singInUseCase.invoke(login, password)
                .onStart { _uiState.value = SignInUiState.Loading }
                .catch { e ->
                    _uiState.value = SignInUiState.Error(code = null, message = e.localizedMessage)
                }
                .collect { result ->
                    _uiState.value = when (result) {
                        is BaseResult.Success -> {
                            SignInUiState.Success
                        }

                        is BaseResult.Error -> {
                            SignInUiState.Error(result.code, result.message)
                        }

                        is BaseResult.Exception -> {
                            SignInUiState.Error(null, result.message)
                        }
                    }
                }
        }
    }

    companion object {

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SignInViewModel(
                    SignInUseCase(AccountRepositoryImpl(RemoteAccountDataSource()))
                )
            }
        }
    }
}