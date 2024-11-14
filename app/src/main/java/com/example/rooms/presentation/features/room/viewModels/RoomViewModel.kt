package com.example.rooms.presentation.features.main.rooms.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.rooms.data.model.rooms.RoomDto
import com.example.rooms.data.model.scramble.ScrambleDto
import com.example.rooms.domain.repository.RoomsRepository
import com.example.rooms.domain.useCases.rooms.CreateRoomUseCase
import com.example.rooms.domain.useCases.rooms.GetRoomsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RoomUiState(
    val room: RoomDto? = null,
    val scramble: ScrambleDto? = null
)

class RoomViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RoomUiState())
    val uiState: StateFlow<RoomUiState> = _uiState.asStateFlow()

    init {
//        _uiState.value = _uiState.value.copy(room = room)
        getScramble()
    }

    fun getScramble() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
//                val puzzle = uiState.value.room?.puzzle ?: 3
//                val response = RetrofitInstance.scrambleApi.getScramble(puzzle = puzzle)
//                val scramble = response.body()
//                _uiState.value = _uiState.value.copy(scramble = scramble)
            } catch (e: Exception) {
                Log.e("TAG", "Exception during request -> ${e.localizedMessage}")
            }
        }
    }

    companion object {
        fun createFactory(

            repository: RoomsRepository
        ) = viewModelFactory {
            initializer {
                RoomViewModel(

                )
            }
        }
    }
}