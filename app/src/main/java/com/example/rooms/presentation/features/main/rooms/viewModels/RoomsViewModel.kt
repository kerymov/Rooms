package com.example.rooms.presentation.features.main.rooms.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.rooms.domain.model.BaseResult
import com.example.rooms.domain.repository.RoomsRepository
import com.example.rooms.domain.useCases.rooms.GetRoomsUseCase
import com.example.rooms.presentation.features.main.rooms.models.Event
import com.example.rooms.presentation.features.main.rooms.models.RoomUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

sealed class RoomsUiState(open val rooms: List<RoomUiModel>?) {

    class Success(override val rooms: List<RoomUiModel>) : RoomsUiState(rooms)
    class Error(rooms: List<RoomUiModel>, val code: Int?, val message: String?) : RoomsUiState(rooms)
    class Loading(rooms: List<RoomUiModel>) : RoomsUiState(rooms)
    data object None : RoomsUiState(rooms = null)
}

class RoomsViewModel(
    private val getRoomsUseCase: GetRoomsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<RoomsUiState>(RoomsUiState.None)
    val uiState: StateFlow<RoomsUiState> = _uiState.asStateFlow()

    init {
        getRooms()
    }

    fun getRooms() {
        viewModelScope.launch(Dispatchers.IO) {
            getRoomsUseCase.invoke()
                .onStart {
                    _uiState.value = RoomsUiState.Loading(rooms = uiState.value.rooms ?: listOf())
                }
                .catch { e ->
                    _uiState.value = RoomsUiState.Error(
                        rooms = uiState.value.rooms ?: listOf(),
                        code = null,
                        message = e.message
                    )
                }
                .collect { result ->
                    _uiState.value = when (result) {
                        is BaseResult.Success -> RoomsUiState.Success(
                            rooms = result.data.map { room ->
                                RoomUiModel(
                                    id = room.id,
                                    name = room.roomName,
                                    event = Event.entries.find { it.id == room.puzzle } ?: Event.THREE_BY_THREE,
                                    isOpen = room.isOpen
                                )
                            }
                        )
                        is BaseResult.Error -> RoomsUiState.Error(
                            rooms = uiState.value.rooms ?: listOf(),
                            code = result.code,
                            message = result.message
                        )
                        is BaseResult.Exception -> RoomsUiState.Error(
                            rooms = uiState.value.rooms ?: listOf(),
                            code = null,
                            message = result.message
                        )
                    }
                }
        }
    }

//    fun getRoomById(id: String): RoomDto? {
//        val room = _uiState.value.rooms.find { it.id == id }
//        _uiState.value = _uiState.value.copy(currentRoom = room)
//        return room
//    }

//    fun createRoom(roomCreationRequest: RoomCreationRequestDto) {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                RetrofitInstance.roomsApi.createRoom(roomCreationRequest)
//            } catch (e: Exception) {
//                Log.e("TAG", "Exception during request -> ${e.localizedMessage}")
//            }
//        }
//    }

//    fun deleteRoom(id: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                RetrofitInstance.roomsApi.deleteRoom(id)
//            } catch (e: Exception) {
//                Log.e("TAG", "Exception during request -> ${e.localizedMessage}")
//            }
//        }
//    }

    companion object {

        fun createFactory(repository: RoomsRepository) = viewModelFactory {
            initializer {
                RoomsViewModel(
                    GetRoomsUseCase(repository)
                )
            }
        }
    }
}