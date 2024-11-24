package com.example.rooms.presentation.features.room.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.rooms.domain.model.rooms.Penalty
import com.example.rooms.domain.repository.RoomRepository
import com.example.rooms.domain.useCases.room.AskForNewSolveUseCase
import com.example.rooms.domain.useCases.room.GetFinishedSolvesUseCase
import com.example.rooms.domain.useCases.room.GetLeftUsersUseCase
import com.example.rooms.domain.useCases.room.GetNewResultsUseCase
import com.example.rooms.domain.useCases.room.GetNewUsersUseCase
import com.example.rooms.domain.useCases.room.GetScrambleUseCase
import com.example.rooms.domain.useCases.room.JoinRoomUseCase
import com.example.rooms.domain.useCases.room.LeaveRoomUseCase
import com.example.rooms.domain.useCases.room.SendSolveResultUseCase
import com.example.rooms.presentation.features.main.rooms.models.NewSolveResultUi
import com.example.rooms.presentation.features.main.rooms.models.PenaltyUi
import com.example.rooms.presentation.features.main.rooms.models.RoomDetailsUi
import com.example.rooms.presentation.features.main.rooms.models.SolveUi
import com.example.rooms.presentation.mappers.mapToDomainModel
import com.example.rooms.presentation.mappers.mapToUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import java.time.Duration

data class RoomUiState(
    val roomDetails: RoomDetailsUi,
    val currentSolve: SolveUi? = null,
    val users: List<String> = emptyList(),
    val solves: List<SolveUi> = emptyList()
)

class RoomViewModel(
    private val roomDetails: RoomDetailsUi,
    private val joinRoomUseCase: JoinRoomUseCase,
    private val leaveRoomUseCase: LeaveRoomUseCase,
    private val getNewUsersUseCase: GetNewUsersUseCase,
    private val getLeftUsersUseCase: GetLeftUsersUseCase,
    private val getFinishedSolvesUseCase: GetFinishedSolvesUseCase,
    private val getNewResultsUseCase: GetNewResultsUseCase,
    private val sendSolveResultUseCase: SendSolveResultUseCase,
    private val askForNewSolveUseCase: AskForNewSolveUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RoomUiState(roomDetails))
    val uiState: StateFlow<RoomUiState> = _uiState.asStateFlow()

    init {
        initUiState()

        joinRoom(roomName = roomDetails.name)

        getNewUsers()
        getLeftUsers()
        getFinishedSolves()
        getNewResults()
    }

    private fun initUiState() {
        _uiState.update { uiState ->
            uiState.copy(
                users = roomDetails.connectedUserNames,
                solves = roomDetails.solves,
                currentSolve = roomDetails.solves.lastOrNull(),
            )
        }
    }

    private fun joinRoom(roomName: String) = viewModelScope.launch(Dispatchers.IO) {
        joinRoomUseCase.invoke(roomName)
    }

    private fun leaveRoom(roomName: String) = viewModelScope.launch(Dispatchers.IO) {
        leaveRoomUseCase.invoke(roomName)
    }

    private fun askForNewSolve(roomId: String) = viewModelScope.launch(Dispatchers.IO) {
        askForNewSolveUseCase.invoke(roomId)
    }

    fun sendSolveResult(time: Long, penalty: PenaltyUi) = viewModelScope.launch(Dispatchers.IO) {
        val result = NewSolveResultUi(
            roomId = _uiState.value.roomDetails.id,
            solveNumber = _uiState.value.currentSolve?.solveNumber ?: 1,
            timeInMilliseconds = time,
            penalty = penalty
        )
        sendSolveResultUseCase.invoke(result.mapToDomainModel())
    }

    private fun getNewUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            getNewUsersUseCase.invoke()
                .collect { user ->
                    val currentUsers = _uiState.value.users
                    _uiState.update { uiState ->
                        uiState.copy(
                            users = if (user.username in currentUsers) {
                                currentUsers
                            } else {
                                currentUsers + user.username
                            }
                        )
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

    private fun getFinishedSolves() {
        viewModelScope.launch(Dispatchers.IO) {
            getFinishedSolvesUseCase.invoke()
                .collect { solve ->
                    val currentSolves = _uiState.value.solves
                    _uiState.update { uiState ->
                        uiState.copy(
                            solves = currentSolves + solve.mapToUiModel(),
                            currentSolve = solve.mapToUiModel()
                        )
                    }
                }
        }
    }

    private fun getNewResults() {
        viewModelScope.launch(Dispatchers.IO) {
            getNewResultsUseCase.invoke()
                .collect { result ->
                    _uiState.update { uiState ->
                        uiState.copy(
                            currentSolve = uiState.currentSolve?.copy(
                                results = uiState.currentSolve.results + result.mapToUiModel()
                            )
                        )
                    }
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        leaveRoom(roomName = roomDetails.name)
    }

    companion object {
        fun createFactory(roomDetails: RoomDetailsUi, repository: RoomRepository) =
            viewModelFactory {
                initializer {
                    RoomViewModel(
                        roomDetails,
                        JoinRoomUseCase(repository),
                        LeaveRoomUseCase(repository),
                        GetNewUsersUseCase(repository),
                        GetLeftUsersUseCase(repository),
                        GetFinishedSolvesUseCase(repository),
                        GetNewResultsUseCase(repository),
                        SendSolveResultUseCase(repository),
                        AskForNewSolveUseCase(repository)
                    )
                }
            }
    }
}