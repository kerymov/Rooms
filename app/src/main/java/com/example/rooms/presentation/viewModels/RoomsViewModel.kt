package com.example.rooms.presentation.viewModels

import androidx.lifecycle.ViewModel

class RoomsViewModel : ViewModel() {

//    var rooms: List<Room> = listOf()
//
//    private val _uiState = MutableStateFlow(RoomsUiState(rooms))
//    val uiState: StateFlow<RoomsUiState> = _uiState.asStateFlow()
//
//    fun addRoom(room: Room) {
//        _uiState.update { currentState ->
//            currentState.copy(
//                rooms = currentState.rooms + room
//            )
//        }
//    }

//    fun getRooms() {
//        viewModelScope.launch {
//            withContext(Dispatchers.IO) {
//                rooms = RetrofitInstance.roomsApi.getRooms()
//                val response = try {
//                    RetrofitInstance.roomsApi.getRooms()
//                } catch (e: Exception) {
//                    Log.e("tag", e.message ?: "")
//                    null
//                }
//
//                if (response?.isSuccessful == true && response.body() != null) {
//                    rooms = response.body()!!.rooms
//                }
//            }
//        }
//    }

//    fun getRoomById(id: Long) = _uiState.value.rooms[0]
}