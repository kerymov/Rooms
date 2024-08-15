package com.example.rooms.presentation.ui.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.rooms.data.dataSource.RemoteAccountDataSource
import com.example.rooms.data.network.RetrofitInstance
import com.example.rooms.data.model.account.auth.UserSignUpRequestDto
import com.example.rooms.data.repository.AccountRepositoryImpl
import com.example.rooms.domain.model.BaseResult
import com.example.rooms.domain.useCases.SignInUseCase
import com.example.rooms.domain.useCases.SignUpUseCase
import com.example.rooms.utils.AppPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class SignUpUiState {
    data object Success : SignUpUiState()
    data class Error(val code: Int?, val message: String?) : SignUpUiState()
    data object Loading : SignUpUiState()
    data object None : SignUpUiState()
}

class SignUpViewModel(
    private val useCase: SignUpUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<SignUpUiState>(SignUpUiState.None)
    val uiState = _uiState.asStateFlow()

    fun signUp(
        login: String,
        password: String,
        passwordConfirm: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = SignUpUiState.Loading

            useCase.invoke(login, password, passwordConfirm)
                .catch { e ->
                    _uiState.value = SignUpUiState.Error(code = null, message = e.localizedMessage)
                }
                .collect { result ->
                    _uiState.value = when (result) {
                        is BaseResult.Success -> SignUpUiState.Success
                        is BaseResult.Error -> SignUpUiState.Error(result.code, result.message)
                        is BaseResult.Exception -> SignUpUiState.Error(null, result.message)
                    }
                }
        }
    }

    companion object {

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SignUpViewModel(
                    SignUpUseCase(AccountRepositoryImpl(RemoteAccountDataSource()))
                )
            }
        }
    }
}