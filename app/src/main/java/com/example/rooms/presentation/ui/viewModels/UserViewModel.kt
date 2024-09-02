package com.example.rooms.presentation.ui.viewModels

import android.content.Context
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

data class UserUiState(
    val isLoading: Boolean = true,
    val isUserAuthorized: Boolean = false
)

class UserViewModel(
    private val getUserUseCase: GetUserUseCase
): ViewModel() {
    var uiState by mutableStateOf(UserUiState())
        private set

    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch {
            val result = viewModelScope.async(Dispatchers.IO) {
                getUserUseCase.invoke().first()
            }

            uiState = when (result.await()) {
                is BaseResult.Success ->
                    UserUiState(isLoading = false, isUserAuthorized = true)
                is BaseResult.Error, is BaseResult.Exception ->
                    UserUiState(isLoading = false, isUserAuthorized = false)
            }
        }
    }

    companion object {

        fun createFactory(context: Context) = viewModelFactory {
            initializer {
                UserViewModel(
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

val LocalUserState = compositionLocalOf<UserViewModel> { error("User State Context Not Found!") }