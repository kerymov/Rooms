package com.example.rooms.presentation.features.main.profile.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.rooms.data.dataSource.LocalAccountDataSource
import com.example.rooms.data.dataSource.RemoteAccountDataSource
import com.example.rooms.data.repository.AccountRepositoryImpl
import com.example.rooms.domain.model.BaseResult
import com.example.rooms.domain.useCases.auth.GetUserUseCase
import com.example.rooms.domain.useCases.auth.SignOutUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

sealed class ProfileUiState {

    open val username: String? = null

    data class Success(override val username: String) : ProfileUiState()
    data class Error(val code: Int?, val message: String?) : ProfileUiState()
    data object Loading : ProfileUiState()
    data object None : ProfileUiState()
}

class ProfileViewModel(
    private val getUserUseCase: GetUserUseCase,
    private val signOutUseCase: SignOutUseCase
): ViewModel() {
    private val _uiState: MutableStateFlow<ProfileUiState> = MutableStateFlow(ProfileUiState.None)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch(Dispatchers.IO) {
            getUserUseCase.invoke()
                .onStart { _uiState.value = ProfileUiState.Loading }
                .catch { e ->
                    _uiState.value = ProfileUiState.Error(code = null, message = e.localizedMessage)
                }
                .collect { result ->
                    _uiState.value = when (result) {
                        is BaseResult.Success -> ProfileUiState.Success(result.data.username)
                        is BaseResult.Error -> ProfileUiState.Error(
                            code = result.code,
                            message = result.message
                        )
                        is BaseResult.Exception -> ProfileUiState.Error(
                            code = null,
                            message = result.message
                        )
                    }
                }
        }
    }

    fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            signOutUseCase.invoke()
        }
    }

    companion object {

        fun createFactory(context: Context) = viewModelFactory {
            initializer {
                val repository = AccountRepositoryImpl(
                    LocalAccountDataSource(context = context),
                    RemoteAccountDataSource(),
                )

                ProfileViewModel(
                    GetUserUseCase(repository),
                    SignOutUseCase(repository),
                )
            }
        }
    }
}