package com.example.rooms.presentation.features.room.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

const val MAX_TIME_LENGTH = 8

class TimerOffsetMapping: OffsetMapping {
    override fun originalToTransformed(offset: Int): Int {
        return offset
    }

    override fun transformedToOriginal(offset: Int): Int {
        return offset
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
            7 -> "${text[0]}:${text[1]}${text[2]}:${text[3]}${text[4]}.${text[5]}${text[6]}"
            else -> "${text[0]}${text[1]}:${text[2]}${text[3]}:${text[4]}${text[5]}.${text[6]}${text[7]}"
        }

        return TransformedText(
            text = AnnotatedString(text = transformedText),
            offsetMapping = TimerOffsetMapping()
        )
    }
}