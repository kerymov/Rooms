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
import com.example.rooms.domain.useCases.auth.SignInUseCase
import com.example.rooms.domain.useCases.auth.SignUpUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

sealed class AuthUiState {

    data object Success : AuthUiState()
    class Error(val code: Int?, val message: String?) : AuthUiState()
    data object Loading : AuthUiState()
    data object None : AuthUiState()
}

class AuthViewModel(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase,
): ViewModel() {
    private val _uiState: MutableStateFlow<AuthUiState> = MutableStateFlow(AuthUiState.None)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun signUp(
        login: String,
        password: String,
        passwordConfirm: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = AuthUiState.Loading

            signUpUseCase.invoke(login, password, passwordConfirm)
                .catch { e ->
                    _uiState.value = AuthUiState.Error(code = null, message = e.localizedMessage)
                }
                .collect { result ->
                    _uiState.value = when (result) {
                        is BaseResult.Success -> AuthUiState.Success
                        is BaseResult.Error -> AuthUiState.Error(code = result.code, message = result.message)
                        is BaseResult.Exception -> AuthUiState.Error(code = null, message = result.message)
                    }
                }
        }
    }

    fun signIn(
        login: String,
        password: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            signInUseCase.invoke(login, password)
                .onStart {
                    _uiState.value = AuthUiState.Loading
                }
                .catch { e ->
                    _uiState.value = AuthUiState.Error(code = null, message = e.message)
                }
                .collect { result ->
                    _uiState.value = when (result) {
                        is BaseResult.Success -> AuthUiState.Success
                        is BaseResult.Error -> AuthUiState.Error(code = result.code, message = result.message)
                        is BaseResult.Exception -> AuthUiState.Error(code = null, message = result.message)
                    }
                }
        }
    }

    fun resetUiState() {
        _uiState.value = AuthUiState.None
    }

    companion object {

        fun createFactory(context: Context) = viewModelFactory {
            initializer {
                val repository = AccountRepositoryImpl(
                    LocalAccountDataSource(context = context),
                    RemoteAccountDataSource(),
                )

                AuthViewModel(
                    SignInUseCase(repository),
                    SignUpUseCase(repository),
                )
            }
        }
    }
}