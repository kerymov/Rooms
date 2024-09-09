package com.example.rooms.presentation.features.main.rooms.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

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

private enum class Duration(val timeInMills: Long, val formatPattern: String) {
    INITIAL_AND_MORE(0, "s.SS"),
    TEN_SECONDS_AND_MORE(10000, "ss.SS"),
    ONE_MINUTE_AND_MORE(60000, "m:ss.SS"),
    TEN_MINUTES_AND_MORE(600000, "mm:ss.SS"),
    ONE_HOUR_AND_MORE(3600000, "h:mm:ss.SS")
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
                formattedTime = formatTime(timeInMills)
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
        formattedTime = formatTime(timeInMills)
    }

    private fun subtractTwoSeconds() {
        timeInMills -= 2000L
        formattedTime = formatTime(timeInMills)
    }

    fun setDnf() {

    }

    private fun formatTime(timeInMills: Long): String {
        val localDateTime = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(timeInMills),
            ZoneId.systemDefault()
        )
        val pattern = when {
            timeInMills < Duration.TEN_SECONDS_AND_MORE.timeInMills -> Duration.INITIAL_AND_MORE.formatPattern
            timeInMills < Duration.ONE_MINUTE_AND_MORE.timeInMills -> Duration.TEN_SECONDS_AND_MORE.formatPattern
            timeInMills < Duration.TEN_MINUTES_AND_MORE.timeInMills -> Duration.ONE_MINUTE_AND_MORE.formatPattern
            timeInMills < Duration.ONE_HOUR_AND_MORE.timeInMills -> Duration.TEN_MINUTES_AND_MORE.formatPattern
            else -> Duration.ONE_HOUR_AND_MORE.formatPattern
        }
        val formatter = DateTimeFormatter.ofPattern(
            pattern,
            Locale.getDefault()
        )
        return localDateTime.format(formatter)
    }
}