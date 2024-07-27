package com.example.rooms.presentation.viewModels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rooms.data.remote.account.models.UserSignInRequest
import com.example.rooms.data.remote.RetrofitInstance
import com.example.rooms.data.remote.account.models.UserSignUpRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class SignInUiState(
    val userName: String? = null,
    val errorMessage: String? = null,
    val isSuccessful: Boolean = false
)

class SignInViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState = _uiState.asStateFlow()

    fun signIn(
        login: String,
        password: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = async(Dispatchers.IO) {
                    RetrofitInstance.accountApi.signIn(UserSignInRequest(login, password))
                }.await()

                val userName = response.body()?.username.orEmpty()
                val errorMessage = response.body()?.errorMessage.orEmpty()
                val isSuccessful = response.body()?.isSuccess ?: false
                
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

    @OptIn(ExperimentalCoroutinesApi::class)
    fun signUp(
        login: String,
        password: String,
        passwordConfirm: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        val result = viewModelScope.async(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.accountApi.signUp(
                    UserSignUpRequest(
                        login,
                        password,
                        passwordConfirm
                    )
                )
                val userName = response.body()?.username.orEmpty()
                val isSignUpSuccess = response.body()?.isSuccess ?: false
                _uiState.value = _uiState.value.copy(userName = userName)

                return@async isSignUpSuccess
            } catch (e: Exception) {
                Log.e("TAG", "Exception during request -> ${e.localizedMessage}")

                return@async false
            }
        }

        result.invokeOnCompletion {
            if (it == null) {
                if (result.getCompleted()) {
                    viewModelScope.launch(Dispatchers.Main) { onSuccess() }
                } else {
                    viewModelScope.launch(Dispatchers.Main) { onFailure() }
                }
            }
        }
    }
}