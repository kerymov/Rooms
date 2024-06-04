package com.example.rooms.presentation.viewModels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rooms.data.remote.RetrofitInstance
import com.example.rooms.data.remote.rooms.models.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RoomsUiState(
    val rooms: List<Room> = listOf(),
    val currentRoom: Room? = null
)

class RoomsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RoomsUiState())
    val uiState: StateFlow<RoomsUiState> = _uiState.asStateFlow()

    init {
        getRooms()
    }

    private fun getRooms() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.roomsApi.getRooms()
                val rooms = response.body() ?: listOf()
                _uiState.value = _uiState.value.copy(rooms = rooms)
            } catch (e: Exception) {
                Log.e("TAG", "Exception during request -> ${e.localizedMessage}")
            }
        }
    }

    fun getRoomById(id: String) {
        val room = _uiState.value.rooms.find { it.id == id }
        _uiState.value = _uiState.value.copy(currentRoom = room)
    }
}