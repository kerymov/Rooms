package com.kerymov.ui_room.utils

import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class TimerRunnerState {
    IDLE,
    ACTIVE,
    STOPPED
}

@ViewModelScoped
class TimerRunner @Inject constructor() {

    private var coroutineScope = CoroutineScope(Dispatchers.Default)

    private val _timerStateFlow = MutableStateFlow(TimerRunnerState.IDLE)
    val timerStateFlow: StateFlow<TimerRunnerState> = _timerStateFlow.asStateFlow()

    private val _timeInMillsFlow = MutableStateFlow(0L)
    val timeInMillsFlow = _timeInMillsFlow.asStateFlow()

    private var lastTimestamp = 0L

    fun start() {
        if (_timerStateFlow.value == TimerRunnerState.ACTIVE) return

        coroutineScope.launch {
            lastTimestamp = System.currentTimeMillis()
            this@TimerRunner._timerStateFlow.update { TimerRunnerState.ACTIVE }

            while (this@TimerRunner._timerStateFlow.value == TimerRunnerState.ACTIVE) {
                delay(10L)
                _timeInMillsFlow.update {
                    it + System.currentTimeMillis() - lastTimestamp
                }
                lastTimestamp = System.currentTimeMillis()
            }
        }
    }

    fun stop() {
        coroutineScope.cancel()
        _timerStateFlow.update { TimerRunnerState.STOPPED }
    }

    fun reset() {
        coroutineScope.cancel()
        coroutineScope = CoroutineScope(Dispatchers.Main)
        _timeInMillsFlow.update { 0L }
        lastTimestamp = 0L
        _timerStateFlow.update { TimerRunnerState.IDLE }
    }
}