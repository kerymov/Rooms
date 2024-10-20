package com.example.rooms.presentation.features.main.profile.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.rooms.domain.repository.AccountRepository
import com.example.rooms.domain.useCases.auth.GetUserUseCase
import com.example.rooms.domain.useCases.auth.SignOutUseCase
import com.example.rooms.presentation.features.auth.models.UserUiModel
import com.example.rooms.presentation.mappers.mapToUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ProfileUiState(open val user: UserUiModel?) {

    data class Success(override val user: UserUiModel) : ProfileUiState(user)
    class Error(user: UserUiModel?, val code: Int?, val message: String?) : ProfileUiState(user)
    class Loading(user: UserUiModel?) : ProfileUiState(user)
    data object None : ProfileUiState(null)
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
            _uiState.value = getUserUseCase.invoke()?.let { user ->
                val userUiModel = user.mapToUiModel()

                ProfileUiState.Success(userUiModel)
            } ?: ProfileUiState.Error(_uiState.value.user, null, "User not authorized")
        }
    }

    fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            signOutUseCase.invoke()
        }
    }

    companion object {

        fun createFactory(repository: AccountRepository) = viewModelFactory {
            initializer {
                ProfileViewModel(
                    GetUserUseCase(repository),
                    SignOutUseCase(repository),
                )
            }
        }
    }
}