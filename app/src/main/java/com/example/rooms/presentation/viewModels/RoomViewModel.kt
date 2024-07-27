package com.example.rooms.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rooms.data.remote.RetrofitInstance
import com.example.rooms.data.remote.rooms.models.Room
import com.example.rooms.data.remote.scramble.models.Image
import com.example.rooms.data.remote.scramble.models.Scramble
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RoomUiState(
    val room: Room? = null,
    val scramble: Scramble? = null
)

class RoomViewModel(private val roomId: String) : ViewModel() {

    private val _uiState = MutableStateFlow(RoomUiState())
    val uiState: StateFlow<RoomUiState> = _uiState.asStateFlow()

    init {
        getScramble()
    }

//    fun getRoomById() {
//        _uiState.value = _uiState.value.copy(room = room)
//    }

    fun getScramble() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val puzzle = uiState.value.room?.puzzle ?: 3
                val response = RetrofitInstance.scrambleApi.getScramble(puzzle = puzzle)
                val scramble = response.body()
                _uiState.value = _uiState.value.copy(scramble = scramble)
            } catch (e: Exception) {
                Log.e("TAG", "Exception during request -> ${e.localizedMessage}")
            }
        }
    }
}