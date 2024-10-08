package com.example.rooms.presentation.features.main.rooms.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rooms.data.network.RetrofitInstance
import com.example.rooms.data.model.rooms.RoomDto
import com.example.rooms.data.model.rooms.RoomCreationRequestDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RoomsUiState(
    val rooms: List<RoomDto> = listOf(),
    val currentRoom: RoomDto? = null
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
                _uiState.update { it.copy(rooms = rooms) }
            } catch (e: Exception) {
                Log.e("TAG", "Exception during request -> ${e.localizedMessage}")
            }
        }
    }

    fun getRoomById(id: String): RoomDto? {
        val room = _uiState.value.rooms.find { it.id == id }
        _uiState.value = _uiState.value.copy(currentRoom = room)
        return room
    }

    fun createRoom(roomCreationRequest: RoomCreationRequestDto) {
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