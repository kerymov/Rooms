package com.example.rooms.presentation.features.room.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.rooms.domain.model.BaseResult
import com.example.rooms.domain.repository.RoomRepository
import com.example.rooms.domain.useCases.room.GetLeftUsersUseCase
import com.example.rooms.domain.useCases.room.JoinRoomUseCase
import com.example.rooms.domain.useCases.room.LeaveRoomUseCase
import com.example.rooms.domain.useCases.room.GetNewUsersUseCase
import com.example.rooms.domain.useCases.room.GetScrambleUseCase
import com.example.rooms.presentation.features.main.rooms.models.RoomDetailsUi
import com.example.rooms.presentation.features.main.rooms.models.ScrambleUi
import com.example.rooms.presentation.mappers.mapToUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RoomUiState(
    val roomDetails: RoomDetailsUi? = null,
    val scramble: ScrambleUi? = null,
    val users: List<String> = listOf()
)

class RoomViewModel(
    private val roomDetails: RoomDetailsUi,
    private val joinRoomUseCase: JoinRoomUseCase,
    private val leaveRoomUseCase: LeaveRoomUseCase,
    private val getNewUsersUseCase: GetNewUsersUseCase,
    private val getLeftUsersUseCase: GetLeftUsersUseCase,
    private val getScrambleUseCase: GetScrambleUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RoomUiState())
    val uiState: StateFlow<RoomUiState> = _uiState.asStateFlow()

    init {
        _uiState.value = _uiState.value.copy(roomDetails = roomDetails)
        joinRoom(roomName = roomDetails.name)

        getNewUsers()
        getLeftUsers()
        getScramble()
    }

    private fun joinRoom(roomName: String) = viewModelScope.launch(Dispatchers.IO) {
        joinRoomUseCase.invoke(roomName)
    }

    private fun leaveRoom(roomName: String) = viewModelScope.launch(Dispatchers.IO) {
        leaveRoomUseCase.invoke(roomName)
    }

    private fun getNewUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            getNewUsersUseCase.invoke()
                .collect { user ->
                    val currentUsers = _uiState.value.users
                    _uiState.update { uiState ->
                        uiState.copy(users = currentUsers + user.username)
                    }
                }
        }
    }

    private fun getLeftUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            getLeftUsersUseCase.invoke()
                .collect { user ->
                    val currentUsers = _uiState.value.users
                    _uiState.update { uiState ->
                        uiState.copy(users = currentUsers.filter { it != user.username })
                    }
                }
        }
    }

    fun getScramble() {
        viewModelScope.launch(Dispatchers.IO) {
            val roomDetailsResult = async { getScrambleUseCase.invoke(roomDetails.settings.event.id) }

            when (val result = roomDetailsResult.await()) {
                is BaseResult.Success -> _uiState.update { uiState ->
                    uiState.copy(scramble = result.data.mapToUiModel())
                }
                is BaseResult.Error -> { }
                is BaseResult.Exception -> { }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        leaveRoom(roomName = roomDetails.name)
    }

    companion object {
        fun createFactory(roomDetails: RoomDetailsUi, repository: RoomRepository) = viewModelFactory {
            initializer {
                RoomViewModel(
                    roomDetails,
                    JoinRoomUseCase(repository),
                    LeaveRoomUseCase(repository),
                    GetNewUsersUseCase(repository),
                    GetLeftUsersUseCase(repository),
                    GetScrambleUseCase(repository)
                )
            }
        }
    }
}