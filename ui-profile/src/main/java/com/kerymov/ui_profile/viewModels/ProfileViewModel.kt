package com.kerymov.ui_profile.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerymov.domain_profile.useCases.GetUserUseCase
import com.kerymov.domain_profile.useCases.SignOutUseCase
import com.kerymov.ui_core.models.UserUi
import com.kerymov.ui_core.models.mappers.UserMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(val user: UserUi? = null)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val userMapper: UserMapper
) : ViewModel() {
    private val _uiState: MutableStateFlow<ProfileUiState> = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch(Dispatchers.IO) {
            getUserUseCase.invoke()
                .collect { user ->
                        user?.let {
                            _uiState.update { it.copy(user = userMapper.mapFromDomain(user)) }
                        } ?: _uiState.update { it.copy(user = null) }
                    }
                }
    }

    fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            signOutUseCase.invoke()
        }
    }
}