package com.example.rooms.presentation.features.room.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

const val MAX_TIME_LENGTH = 7

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

class TimerOffsetMapping: OffsetMapping {
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

class TimerVisualTransformation: VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed =
            if (text.text.length >= MAX_TIME_LENGTH) {
                text.text.substring(0..< MAX_TIME_LENGTH)
            } else {
                text.text
            }

        val transformedText = when (trimmed.length) {
            0 -> "0.00"
            1 -> "0.0${text[0]}"
            2 -> "0.${text[0]}${text[1]}"
            3 -> "${text[0]}.${text[1]}${text[2]}"
            4 -> "${text[0]}${text[1]}.${text[2]}${text[3]}"
            5 -> "${text[0]}:${text[1]}${text[2]}.${text[3]}${text[4]}"
            6 -> "${text[0]}${text[1]}:${text[2]}${text[3]}.${text[4]}${text[5]}"
            else -> "${text[0]}:${text[1]}${text[2]}:${text[3]}${text[4]}.${text[5]}${text[6]}"
        }

        return TransformedText(
            text = AnnotatedString(text = transformedText),
            offsetMapping = TimerOffsetMapping()
        )
    }
}