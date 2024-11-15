package com.example.rooms.presentation.features.main.rooms.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.rooms.domain.model.BaseResult
import com.example.rooms.domain.repository.RoomsRepository
import com.example.rooms.domain.useCases.rooms.CreateRoomUseCase
import com.example.rooms.domain.useCases.rooms.GetRoomsUseCase
import com.example.rooms.domain.useCases.rooms.LoginRoomUseCase
import com.example.rooms.presentation.features.main.rooms.models.RoomDetailsUi
import com.example.rooms.presentation.features.main.rooms.models.RoomUi
import com.example.rooms.presentation.features.main.rooms.models.SettingsUi
import com.example.rooms.presentation.mappers.mapToDomainModel
import com.example.rooms.presentation.mappers.mapToUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

sealed class RoomsUiState(
    open val rooms: List<RoomUi> = emptyList(),
    open val currentRoomDetails: RoomDetailsUi? = null
) {

    class Success(
        override val rooms: List<RoomUi> = emptyList(),
        override val currentRoomDetails: RoomDetailsUi? = null
    ) : RoomsUiState(rooms = rooms, currentRoomDetails = currentRoomDetails)
    data class Error(val code: Int?, val message: String?) : RoomsUiState(rooms = emptyList(), currentRoomDetails = null)
    data object Loading : RoomsUiState(rooms = emptyList(), currentRoomDetails = null)
    data object None : RoomsUiState(rooms = emptyList(), currentRoomDetails = null)
}

class RoomsViewModel(
    private val getRoomsUseCase: GetRoomsUseCase,
    private val createRoomUseCase: CreateRoomUseCase,
    private val loginRoomUseCase: LoginRoomUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<RoomsUiState>(RoomsUiState.None)
    val uiState: StateFlow<RoomsUiState> = _uiState.asStateFlow()

    init {
        getRooms()
    }

    fun getRooms() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = RoomsUiState.Loading

            getRoomsUseCase.invoke()
                .catch { e ->
                    _uiState.value = RoomsUiState.Error(
                        code = null,
                        message = e.message
                    )
                }
                .collect { result ->
                    _uiState.value = when (result) {
                        is BaseResult.Success -> RoomsUiState.Success(
                            rooms = result.data.map { it.mapToUiModel() },
                        )
                        is BaseResult.Error -> RoomsUiState.Error(
                            code = result.code,
                            message = result.message
                        )
                        is BaseResult.Exception -> RoomsUiState.Error(
                            code = null,
                            message = result.message
                        )
                    }
                }
        }
    }

    fun createRoom(
        name: String,
        password: String? = null,
        settings: SettingsUi
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = RoomsUiState.Loading

            val roomDetailsResult = async {createRoomUseCase.invoke(name, password, settings.mapToDomainModel()) }

            _uiState.value = when (val result = roomDetailsResult.await()) {
                is BaseResult.Success -> RoomsUiState.Success(
                    rooms = _uiState.value.rooms,
                    currentRoomDetails = result.data.mapToUiModel()
                )

                is BaseResult.Error -> RoomsUiState.Error(
                    code = result.code,
                    message = result.message
                )

                is BaseResult.Exception -> RoomsUiState.Error(
                    code = null,
                    message = result.message
                )
            }
        }
    }

    fun loginRoom(name: String, password: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = RoomsUiState.Loading

            val roomDetailsResult = async {loginRoomUseCase.invoke(name, password) }

            _uiState.value = when (val result = roomDetailsResult.await()) {
                is BaseResult.Success -> RoomsUiState.Success(
                    rooms = _uiState.value.rooms,
                    currentRoomDetails = result.data.mapToUiModel()
                )

                is BaseResult.Error -> RoomsUiState.Error(
                    code = result.code,
                    message = result.message
                )

                is BaseResult.Exception -> RoomsUiState.Error(
                    code = null,
                    message = result.message
                )
            }
        }
    }

//    fun getRoomById(id: String): RoomDto? {
//        val room = _uiState.value.rooms.find { it.id == id }
//        _uiState.value = _uiState.value.copy(currentRoom = room)
//        return room
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
                    GetRoomsUseCase(repository),
                    CreateRoomUseCase(repository),
                    LoginRoomUseCase(repository)
                )
            }
        }
    }
}