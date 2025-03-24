package com.example.ui_onboarding.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.domain_core.utils.BaseResult
import com.example.domain_onboarding.repository.AccountRepository
import com.example.domain_onboarding.useCases.SignInUseCase
import com.example.domain_onboarding.useCases.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthUiState {

    data object Success : AuthUiState()
    class Error(val code: Int?, val message: String?) : AuthUiState()
    data object Loading : AuthUiState()
    data object None : AuthUiState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
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

            val result = signUpUseCase.invoke(login, password, passwordConfirm)

            _uiState.value = when (result) {
                is BaseResult.Success -> AuthUiState.Success
                is BaseResult.Error -> AuthUiState.Error(
                    code = result.code,
                    message = result.message
                )
                is BaseResult.Exception -> AuthUiState.Error(
                    code = null,
                    message = result.message
                )
            }
        }
    }

    fun signIn(
        login: String,
        password: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = AuthUiState.Loading

            val result = signInUseCase.invoke(login, password)

            _uiState.value = when (result) {
                is BaseResult.Success -> AuthUiState.Success
                is BaseResult.Error -> AuthUiState.Error(
                    code = result.code,
                    message = result.message
                )
                is BaseResult.Exception -> AuthUiState.Error(
                    code = null,
                    message = result.message
                )
            }
        }
    }

    fun resetUiState() {
        _uiState.value = AuthUiState.None
    }
}