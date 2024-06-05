package com.example.rooms.presentation.viewModels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rooms.data.remote.RetrofitInstance
import com.example.rooms.data.remote.rooms.models.Room
import com.example.rooms.data.remote.rooms.models.RoomCreationRequest
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

    fun getRooms() {
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

    fun createRoom(roomCreationRequest: RoomCreationRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                RetrofitInstance.roomsApi.createRoom(roomCreationRequest)
            } catch (e: Exception) {
                Log.e("TAG", "Exception during request -> ${e.localizedMessage}")
            }
        }
    }

    fun deleteRoom(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                RetrofitInstance.roomsApi.deleteRoom(id)
            } catch (e: Exception) {
                Log.e("TAG", "Exception during request -> ${e.localizedMessage}")
            }
        }
    }
}