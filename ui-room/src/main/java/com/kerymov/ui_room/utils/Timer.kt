package com.kerymov.ui_room.utils

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
    ACTIVE,
    STOPPED
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
        if (state == TimerState.ACTIVE) return

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
        coroutineScope.cancel()
        state = TimerState.STOPPED
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

    fun setPlusTwoPenalty() {
        if (penalty == Penalty.PLUS_TWO) return

        penalty = Penalty.PLUS_TWO
        addTwoSeconds()
    }

    fun setDnfPenalty() {
        when (penalty) {
            Penalty.DNF -> return
            Penalty.PLUS_TWO -> subtractTwoSeconds()
            Penalty.NO_PENALTY -> Unit
        }

        penalty = Penalty.DNF
    }

    fun removePenalty() {
        when (penalty) {
            Penalty.NO_PENALTY -> return
            Penalty.PLUS_TWO -> subtractTwoSeconds()
            Penalty.DNF -> Unit
        }

        penalty = Penalty.NO_PENALTY
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