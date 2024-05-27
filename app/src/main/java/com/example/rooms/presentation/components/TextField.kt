package com.example.rooms.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    isError: Boolean
) = OutlinedTextField(
    value = value,
    onValueChange = onValueChange,
    textStyle = MaterialTheme.typography.bodyLarge,
    colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
        focusedTextColor = MaterialTheme.colorScheme.onBackground,
        unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
        cursorColor = MaterialTheme.colorScheme.onBackground,
        focusedPlaceholderColor = MaterialTheme.colorScheme.outlineVariant,
        unfocusedPlaceholderColor = MaterialTheme.colorScheme.outlineVariant,
        errorBorderColor = MaterialTheme.colorScheme.error
    ),
    shape = RoundedCornerShape(8.dp),
    placeholder = {
        Text(
            text = placeholderText,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.outlineVariant
        )
    },
    singleLine = true,
    isError = isError,
    modifier = Modifier.fillMaxWidth()
)