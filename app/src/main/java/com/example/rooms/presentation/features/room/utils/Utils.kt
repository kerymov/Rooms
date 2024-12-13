package com.example.rooms.presentation.features.room.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.example.rooms.presentation.features.main.rooms.models.PenaltyUi
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

const val RESULT_PLACEHOLDER = "--:--"

const val MILLISECONDS_IN_SECOND = 1000
const val MILLISECONDS_IN_MINUTE = MILLISECONDS_IN_SECOND * 60
const val MILLISECONDS_IN_HOUR = MILLISECONDS_IN_MINUTE * 60

const val MAX_TIME_LENGTH = 7

private enum class SolveDuration(val timeInMills: Long, val formatPattern: String) {
    INITIAL_AND_MORE(0, "s.SS"),
    TEN_SECONDS_AND_MORE(10000, "ss.SS"),
    ONE_MINUTE_AND_MORE(60000, "m:ss.SS"),
    TEN_MINUTES_AND_MORE(600000, "mm:ss.SS"),
    ONE_HOUR_AND_MORE(3600000, "h:mm:ss.SS")
}

private enum class TimeRange(val originalTextOffset: Int, val transformedTextOffset: Int) {
    INITIAL(originalTextOffset = 0, transformedTextOffset = 4),
    UNDER_TEN_MILLISECONDS(originalTextOffset = 1, transformedTextOffset = 4),
    UNDER_ONE_SECOND(originalTextOffset = 2, transformedTextOffset = 4),
    UNDER_TEN_SECONDS(originalTextOffset = 3, transformedTextOffset = 4),
    UNDER_ONE_MINUTE(originalTextOffset = 4, transformedTextOffset = 5),
    UNDER_TEN_MINUTES(originalTextOffset = 5, transformedTextOffset = 7),
    UNDER_ONE_HOUR(originalTextOffset = 6, transformedTextOffset = 8),
    UNDER_TEN_HOURS(originalTextOffset = 7, transformedTextOffset = 10)
}

internal class TimerOffsetMapping: OffsetMapping {
    override fun originalToTransformed(offset: Int): Int {
        val timeRange = TimeRange.entries.find { it.originalTextOffset == offset }
        val transformedTextOffset = timeRange?.transformedTextOffset ?: 0

        return transformedTextOffset
    }

    override fun transformedToOriginal(offset: Int): Int {
        val timeRange = TimeRange.entries.find { it.transformedTextOffset == offset }
        val originalTextOffset = timeRange?.originalTextOffset ?: 0

        return originalTextOffset
    }
}

internal class TimerVisualTransformation: VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed =
            if (text.text.length >= MAX_TIME_LENGTH) {
                text.text.substring(0..< MAX_TIME_LENGTH)
            } else {
                text.text
            }

        val formattedText = trimmed.formatStringTimeToStopwatchPattern()

        return TransformedText(
            text = AnnotatedString(text = formattedText),
            offsetMapping = TimerOffsetMapping()
        )
    }
}

internal fun String.formatStringTimeToStopwatchPattern() = when (this.length) {
    0 -> "0.00"
    1 -> "0.0${this[0]}"
    2 -> "0.${this[0]}${this[1]}"
    3 -> "${this[0]}.${this[1]}${this[2]}"
    4 -> "${this[0]}${this[1]}.${this[2]}${this[3]}"
    5 -> "${this[0]}:${this[1]}${this[2]}.${this[3]}${this[4]}"
    6 -> "${this[0]}${this[1]}:${this[2]}${this[3]}.${this[4]}${this[5]}"
    else -> "${this[0]}:${this[1]}${this[2]}:${this[3]}${this[4]}.${this[5]}${this[6]}"
}

internal fun String.formatRawStringTimeToMills(): Long {
    val timeInStopwatchPattern = this.formatStringTimeToStopwatchPattern()
    val timeInMills = timeInStopwatchPattern.formatTimeToMills()

    return timeInMills
}

internal fun Long.formatTimeFromMills(): String {
    val timeInMills = this

    val localDateTime = LocalDateTime.ofInstant(
        Instant.ofEpochMilli(timeInMills),
        ZoneId.systemDefault()
    )
    val pattern = when {
        timeInMills < SolveDuration.TEN_SECONDS_AND_MORE.timeInMills -> SolveDuration.INITIAL_AND_MORE.formatPattern
        timeInMills < SolveDuration.ONE_MINUTE_AND_MORE.timeInMills -> SolveDuration.TEN_SECONDS_AND_MORE.formatPattern
        timeInMills < SolveDuration.TEN_MINUTES_AND_MORE.timeInMills -> SolveDuration.ONE_MINUTE_AND_MORE.formatPattern
        timeInMills < SolveDuration.ONE_HOUR_AND_MORE.timeInMills -> SolveDuration.TEN_MINUTES_AND_MORE.formatPattern
        else -> SolveDuration.ONE_HOUR_AND_MORE.formatPattern
    }
    val formatter = DateTimeFormatter.ofPattern(
        pattern,
        Locale.getDefault()
    )
    return localDateTime.format(formatter)
}

internal fun String.formatTimeToMills(): Long {
    val timeString = this

    val milliseconds = timeString
        .takeLastWhile { it != '.' }
        .toLongOrNull() ?: 0L
    val seconds = timeString
        .dropLast(3)
        .takeLastWhile { it != ':' }
        .toLongOrNull() ?: 0L
    val minutes = timeString
        .dropLast(6)
        .takeLastWhile { it != ':' }
        .toLongOrNull() ?: 0L
    val hours = timeString
        .dropLast(9)
        .toLongOrNull() ?: 0L

    return hours * MILLISECONDS_IN_HOUR +
            minutes * MILLISECONDS_IN_MINUTE +
            seconds * MILLISECONDS_IN_SECOND +
            milliseconds * 10
}

internal fun resultInWcaNotation(time: Long, penalty: PenaltyUi): String {
    return when(penalty) {
        PenaltyUi.PLUS_TWO -> time.formatTimeFromMills() + "+"
        PenaltyUi.DNF -> "DNF(${time.formatTimeFromMills()})"
        else -> time.formatTimeFromMills()
    }
}

internal fun StatisticInfo.toWcaNotation(): String = when(this) {
    is Completed -> time.formatTimeFromMills()
    is DnfSingle -> "DNF"
    is DnfAverage -> "DNF"
    is NoValue -> "--:--"
}