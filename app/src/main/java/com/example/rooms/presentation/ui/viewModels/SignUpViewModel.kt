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
import com.example.rooms.domain.useCases.auth.SignUpUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

sealed class SignUpUiState {
    data object Success : SignUpUiState()
    data class Error(val code: Int?, val message: String?) : SignUpUiState()
    data object Loading : SignUpUiState()
    data object None : SignUpUiState()
}

class SignUpViewModel(
    private val useCase: SignUpUseCase,
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

        fun createFactory(context: Context) = viewModelFactory {
            initializer {
                SignUpViewModel(
                    SignUpUseCase(
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