package com.example.rooms.presentation.ui.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rooms.data.network.RetrofitInstance
import com.example.rooms.data.model.account.auth.UserSignUpRequestDto
import com.example.rooms.utils.AppPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SignUpUiState(
    val userName: String? = null,
    val errorMessage: String? = null,
    val isSuccessful: Boolean = false
)

class SignUpViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState = _uiState.asStateFlow()

    fun signUp(
        login: String,
        password: String,
        passwordConfirm: String
    ) {
        viewModelScope.launch {
            try {
                val response = async(Dispatchers.IO) {
                    RetrofitInstance.accountApi.signUp(UserSignUpRequestDto(login, password, passwordConfirm))
                }.await()

                val userName = response.body()?.username.orEmpty()
                val errorMessage = response.body()?.errorMessage.orEmpty()
                val isSuccessful = response.body()?.isSuccess ?: false
                val token = response.body()?.token

                AppPreferences.accessToken = token

                _uiState.update { currentState ->
                    currentState.copy(
                        userName = userName,
                        isSuccessful = isSuccessful,
                        errorMessage = errorMessage
                    )
                }
            } catch (e: Exception) {
                Log.e("TAG", "Exception during request -> ${e.localizedMessage}")
            }
        }
    }
}