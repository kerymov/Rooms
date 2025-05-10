package com.example.ui_room.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.domain_room.repository.RoomRepository
import com.example.domain_room.useCases.AskForNewSolveUseCase
import com.example.domain_room.useCases.GetFinishedSolvesUseCase
import com.example.domain_room.useCases.GetLeftUsersUseCase
import com.example.domain_room.useCases.GetNewResultsUseCase
import com.example.domain_room.useCases.GetNewUsersUseCase
import com.example.domain_room.useCases.JoinRoomUseCase
import com.example.domain_room.useCases.LeaveRoomUseCase
import com.example.domain_room.useCases.SendSolveResultUseCase
import com.example.ui_common_speedcubing.mappers.mapToUiModel
import com.example.ui_common_speedcubing.models.PenaltyUi
import com.example.ui_common_speedcubing.models.SolveUi
import com.example.ui_room.mappers.mapToDomainModel
import com.example.ui_room.models.NewSolveResultUi
import com.example.ui_room.models.RoomDetailsUi
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RoomUiState(
    val roomDetails: RoomDetailsUi,
    val currentSolve: SolveUi? = null,
    val isWaitingForNewScramble: Boolean = true,
    val users: List<String> = emptyList(),
    val solves: List<SolveUi> = emptyList()
)

@HiltViewModel(assistedFactory = RoomViewModel.Factory::class)
class RoomViewModel @AssistedInject constructor(
    @Assisted private val roomDetails: RoomDetailsUi,
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

        joinRoom(roomName = roomDetails.name) {
            askForNewSolve(roomId = roomDetails.id)
        }

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
                isWaitingForNewScramble = roomDetails.solves.lastOrNull() == null
            )
        }
    }

    fun sendSolveResult(time: Long, penalty: PenaltyUi) = viewModelScope.launch(Dispatchers.IO) {
        val result = NewSolveResultUi(
            roomId = _uiState.value.roomDetails.id,
            solveNumber = _uiState.value.currentSolve?.solveNumber ?: 1,
            timeInMilliseconds = time,
            penalty = penalty
        )
        sendSolveResultUseCase.invoke(result.mapToDomainModel())
        _uiState.update { it.copy(isWaitingForNewScramble = true) }
    }

    private fun joinRoom(roomName: String, onComplete: () -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        joinRoomUseCase.invoke(roomName, onComplete)
    }

    private fun leaveRoom(roomName: String) = viewModelScope.launch(Dispatchers.IO) {
        leaveRoomUseCase.invoke(roomName)
    }

    private fun askForNewSolve(roomId: String) = viewModelScope.launch(Dispatchers.IO) {
        askForNewSolveUseCase.invoke(roomId)
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
                            currentSolve = solve.mapToUiModel(),
                            isWaitingForNewScramble = false
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

    @AssistedFactory
    interface Factory {
        fun create(roomDetails: RoomDetailsUi): RoomViewModel
    }

//    companion object {
//        fun createFactory(roomDetails: RoomDetailsUi, repository: RoomRepository) =
//            viewModelFactory {
//                initializer {
//                    RoomViewModel(
//                        roomDetails,
//                        JoinRoomUseCase(repository),
//                        LeaveRoomUseCase(repository),
//                        GetNewUsersUseCase(repository),
//                        GetLeftUsersUseCase(repository),
//                        GetFinishedSolvesUseCase(repository),
//                        GetNewResultsUseCase(repository),
//                        SendSolveResultUseCase(repository),
//                        AskForNewSolveUseCase(repository)
//                    )
//                }
//            }
//    }
}