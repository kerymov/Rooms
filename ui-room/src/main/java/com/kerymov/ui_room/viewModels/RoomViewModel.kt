package com.kerymov.ui_room.viewModels

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerymov.domain_common_speedcubing.utils.PenaltyManager
import com.kerymov.ui_common_speedcubing.utils.TimerPenaltyManager
import com.kerymov.domain_room.useCases.AskForNewSolveUseCase
import com.kerymov.domain_room.useCases.GetFinishedSolvesUseCase
import com.kerymov.domain_room.useCases.GetLeftUsersUseCase
import com.kerymov.domain_room.useCases.GetNewResultsUseCase
import com.kerymov.domain_room.useCases.GetNewUsersUseCase
import com.kerymov.domain_room.useCases.JoinRoomUseCase
import com.kerymov.domain_room.useCases.LeaveRoomUseCase
import com.kerymov.domain_room.useCases.SendSolveResultUseCase
import com.kerymov.ui_common_speedcubing.mappers.mapToDomainModel
import com.kerymov.ui_common_speedcubing.mappers.mapToUiModel
import com.kerymov.ui_common_speedcubing.models.PenaltyUi
import com.kerymov.ui_common_speedcubing.models.SolveUi
import com.kerymov.ui_room.mappers.mapToDomainModel
import com.kerymov.ui_room.models.NewSolveResultUi
import com.kerymov.ui_room.models.RoomDetailsUi
import com.kerymov.ui_room.utils.MAX_TIME_LENGTH
import com.kerymov.ui_room.utils.TimerRunner
import com.kerymov.ui_room.utils.TimerRunnerState
import com.kerymov.ui_room.utils.formatRawStringTimeToMills
import com.kerymov.ui_room.utils.formatStringTimeToStopwatchPattern
import com.kerymov.ui_room.utils.formatTimeFromMills
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Immutable
enum class TimerMode(val label: String) {
    TIMER("Timer"),
    TYPING("Typing")
}

@Immutable
sealed interface TimerState {
    val timeInMilliseconds: Long
    val penalty: PenaltyUi
}

@Immutable
data class Timer(
    override val timeInMilliseconds: Long = 0L,
    override val penalty: PenaltyUi = PenaltyUi.NO_PENALTY,
    val formattedTime: String = timeInMilliseconds.formatTimeFromMills(),
    val runnerState: TimerRunnerState = TimerRunnerState.IDLE
) : TimerState, PenaltyManager by TimerPenaltyManager() {

    fun setPenalty(newPenalty: PenaltyUi): Timer {
        val timeWithPenalty = updateTimeWithPenalty(
            time = timeInMilliseconds,
            currentPenalty = penalty.mapToDomainModel(),
            newPenalty = newPenalty.mapToDomainModel()
        )

        return this.copy(
            timeInMilliseconds = timeWithPenalty,
            formattedTime = timeWithPenalty.formatTimeFromMills(),
            penalty = newPenalty,
        )
    }
}

@Immutable
data class Typing(
    val inputTimeText: String = "",
    val formattedTime: String = inputTimeText.formatStringTimeToStopwatchPattern(),
    val isInEditingMode: Boolean = false,
    override val penalty: PenaltyUi = PenaltyUi.NO_PENALTY,
) : TimerState {

    override val timeInMilliseconds: Long
        get() = inputTimeText.formatRawStringTimeToMills()

    fun toggleEditing(isEditing: Boolean): Typing {
        return this.copy(isInEditingMode = isEditing)
    }

    fun setPenalty(penalty: PenaltyUi): Typing {
        return this.copy(penalty = penalty)
    }
}

@Immutable
data class RoomUiState(
    val roomDetails: RoomDetailsUi,
    val currentSolve: SolveUi? = null,
    val timerMode: TimerMode = TimerMode.TIMER,
    val timerState: Timer = Timer(),
    val typingState: Typing = Typing(),
    val isWaitingForNewScramble: Boolean = true,
    val isActionButtonsVisible: Boolean = false,
    val isExitConfirmationDialogShown: Boolean = false,
    val isResultsSheetOpen: Boolean = false,
    val users: List<String> = emptyList(),
    val solves: List<SolveUi> = emptyList()
)

@HiltViewModel(assistedFactory = RoomViewModel.Factory::class)
class RoomViewModel @AssistedInject constructor(
    @Assisted private val roomDetails: RoomDetailsUi,
    private val timerRunner: TimerRunner,
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

        getTimerRunnerState()
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

    fun sendSolveResult() = viewModelScope.launch(Dispatchers.IO) {
        val result = with(_uiState.value) {
            val (timeInMilliseconds, penalty) = when (timerMode) {
                TimerMode.TIMER -> timerState.timeInMilliseconds to timerState.penalty
                TimerMode.TYPING -> typingState.timeInMilliseconds to typingState.penalty
            }

            NewSolveResultUi(
                roomId = roomDetails.id,
                solveNumber = currentSolve?.solveNumber ?: 1,
                timeInMilliseconds = timeInMilliseconds,
                penalty = penalty
            )
        }
        sendSolveResultUseCase.invoke(result.mapToDomainModel())
        _uiState.update {
            it.copy(
                isWaitingForNewScramble = true,
                timerState = Timer(),
                typingState = Typing()
            )
        }
    }

    private fun joinRoom(roomName: String, onComplete: () -> Unit) =
        viewModelScope.launch(Dispatchers.IO) {
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
                    val finishedSolves = _uiState.value.solves
                    _uiState.update { uiState ->
                        uiState.copy(
                            solves = finishedSolves + solve.mapToUiModel(),
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
                            ),
                            solves = uiState.solves.mapIndexed { index, solveUi ->
                                if (index == uiState.solves.lastIndex) {
                                    solveUi.copy(results = solveUi.results + result.mapToUiModel())
                                } else {
                                    solveUi
                                }
                            }
                        )
                    }

                }
        }
    }

    private var timerJob: Job? = null

    fun startTimer() = timerRunner.start()

    fun stopTimer() = timerRunner.stop()

    fun resetTimer() = timerRunner.reset()

    private fun getTimerRunnerState() {
        timerJob = viewModelScope.launch {
            combine(
                timerRunner.timerStateFlow,
                timerRunner.timeInMillsFlow
            ) { timerState, timeInMills ->
                Timer(
                    runnerState = timerState,
                    timeInMilliseconds = timeInMills,
                )
            }
                .distinctUntilChanged { old, new -> old == new }
                .collectLatest { newTimerState ->
                    _uiState.update {
                        it.copy(timerState = newTimerState)
                    }
                }
        }
    }

    fun updateTypingInputTimeText(value: String) {
        val filtered = value.filter { it.isDigit() }
        val trimmed = if (filtered.length >= MAX_TIME_LENGTH) {
            filtered.substring(0..<MAX_TIME_LENGTH)
        } else {
            filtered
        }

        _uiState.update {
            it.copy(
                typingState = _uiState.value.typingState.copy(
                    inputTimeText = trimmed,
                    formattedTime = trimmed.formatStringTimeToStopwatchPattern()
                )
            )
        }
    }

    fun toggleTypingEditingMode(isEditing: Boolean) {
        _uiState.update {
            it.copy(
                typingState = it.typingState.toggleEditing(isEditing)
            )
        }
    }

    fun updatePenalty(newPenalty: PenaltyUi) {
        when (_uiState.value.timerMode) {
            TimerMode.TIMER -> {
                val newTimerState = _uiState.value.timerState.setPenalty(newPenalty)

                _uiState.update { state ->
                    state.copy(timerState = newTimerState)
                }
            }

            TimerMode.TYPING -> {
                val newTypingState = _uiState.value.typingState.setPenalty(newPenalty)

                _uiState.update { state ->
                    state.copy(typingState = newTypingState)
                }
            }
        }
    }

    fun toggleTimerMode(mode: TimerMode) {
        _uiState.update { state ->
            state.copy(timerMode = mode)
        }
        resetTimerAndTypingStates()
        toggleActionButtonsVisibility(false)

        if (mode == TimerMode.TIMER) {
            getTimerRunnerState()
        }
    }

    private fun resetTimerAndTypingStates() {
        timerJob?.cancel()
        resetTimer()

        _uiState.update { uiState ->
            uiState.copy(
                timerState = Timer(),
                typingState = Typing()
            )
        }
    }

    fun toggleActionButtonsVisibility(isVisible: Boolean) {
        _uiState.update { uiState ->
            uiState.copy(isActionButtonsVisible = isVisible)
        }
    }

    fun toggleExitConfirmationDialog(isShown: Boolean) {
        _uiState.update { uiState ->
            uiState.copy(isExitConfirmationDialogShown = isShown)
        }
    }

    fun toggleResultsSheet(isShown: Boolean) {
        _uiState.update { uiState ->
            uiState.copy(isResultsSheetOpen = isShown)
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
}