package com.example.rooms.presentation.viewModels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rooms.data.remote.account.models.UserLoginRequest
import com.example.rooms.data.remote.RetrofitInstance
import com.example.rooms.data.remote.account.models.UserResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AccountUiState(
    val userName: String = "",
    val results: UserResults = UserResults(
        bestResultsByPuzzle = listOf(),
        allResultsByPuzzle = listOf()
    )
)

class AccountViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState: StateFlow<AccountUiState> = _uiState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    fun login(
        login: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        val result = viewModelScope.async(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.accountApi.signIn(UserLoginRequest(login, password))
                val userName = response.body()?.username.orEmpty()
                _uiState.value = _uiState.value.copy(userName = userName)

                return@async response.isSuccessful
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

    fun getResults() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.accountApi.getResults()
                val userResults = response.body() ?: UserResults(
                    bestResultsByPuzzle = listOf(),
                    allResultsByPuzzle = listOf()
                )
                _uiState.value = _uiState.value.copy(results = userResults)
            } catch (e: Exception) {
                Log.e("TAG", "Exception during request -> ${e.localizedMessage}")
            }
        }
    }
}