package com.example.rooms.presentation.features.room.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class TimerState {
    IDLE,
    READY_TO_START,
    ACTIVE
}

enum class Penalty {
    NO_PENALTY,
    PLUS_TWO,
    DNF
}

class Timer {

    var formattedTime by mutableStateOf("0.00")
        private set

    var state by mutableStateOf(TimerState.IDLE)
        private set

    var penalty by mutableStateOf(Penalty.NO_PENALTY)
        private set

    private var coroutineScope = CoroutineScope(Dispatchers.Main)

    var timeInMills = 0L
        private set
    private var lastTimestamp = 0L

    fun start() {
        if (state == TimerState.READY_TO_START) return

        coroutineScope.launch {
            lastTimestamp = System.currentTimeMillis()
            this@Timer.state = TimerState.ACTIVE

            while (this@Timer.state == TimerState.ACTIVE) {
                delay(10L)
                timeInMills += System.currentTimeMillis() - lastTimestamp
                lastTimestamp = System.currentTimeMillis()
                formattedTime = timeInMills.formatTimeFromMills()
            }
        }
    }

    fun stop() {
        state = TimerState.READY_TO_START
    }

    fun reset() {
        coroutineScope.cancel()
        coroutineScope = CoroutineScope(Dispatchers.Main)
        timeInMills = 0L
        lastTimestamp = 0L
        formattedTime = "0.00"
        state = TimerState.IDLE
        penalty = Penalty.NO_PENALTY
    }

    fun managePlusTwoPenalty() = when (penalty) {
        Penalty.NO_PENALTY, Penalty.DNF -> {
            penalty = Penalty.PLUS_TWO
            addTwoSeconds()
        }

        Penalty.PLUS_TWO -> {
            penalty = Penalty.NO_PENALTY
            subtractTwoSeconds()
        }
    }

    fun manageDnfPenalty() = when (penalty) {
        Penalty.NO_PENALTY -> {
            penalty = Penalty.DNF
        }

        Penalty.PLUS_TWO -> {
            penalty = Penalty.DNF
            subtractTwoSeconds()
        }

        Penalty.DNF -> {
            penalty = Penalty.NO_PENALTY
        }
    }

    private fun addTwoSeconds() {
        timeInMills += 2000L
        formattedTime = timeInMills.formatTimeFromMills()
    }

    private fun subtractTwoSeconds() {
        timeInMills -= 2000L
        formattedTime = timeInMills.formatTimeFromMills()
    }
}