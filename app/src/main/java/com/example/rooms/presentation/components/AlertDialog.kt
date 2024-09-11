package com.example.rooms.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

data class CustomAlertDialogColors(
    val titleTextColor: Color,
    val messageTextColor: Color,
    val confirmButtonColor: Color,
    val dismissButtonColor: Color
)

object CustomAlertDialogDefaults {

    @Composable
    fun alertColors(
        titleTextColor: Color = MaterialTheme.colorScheme.onBackground,
        messageTextColor: Color = MaterialTheme.colorScheme.onBackground,
        confirmButtonColor: Color = MaterialTheme.colorScheme.onBackground,
        dismissButtonColor: Color = MaterialTheme.colorScheme.onBackground
    ) = CustomAlertDialogColors(
        titleTextColor = titleTextColor,
        messageTextColor = messageTextColor,
        confirmButtonColor = confirmButtonColor,
        dismissButtonColor = dismissButtonColor
    )
}

@Composable
fun CustomAlertDialog(
    title: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    confirmButtonText: String,
    dismissButtonText: String,
    message: String? = null,
    colors: CustomAlertDialogColors = CustomAlertDialogDefaults.alertColors()
) = AlertDialog(
    title = {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = colors.titleTextColor
        )
    },
    text = {
        message?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyLarge,
                color = colors.messageTextColor,
            )
        }
    },
    onDismissRequest = onDismiss,
    confirmButton = {
        TextButton(onClick = onConfirm) {
            Text(
                text = confirmButtonText,
                style = MaterialTheme.typography.titleMedium,
                color = colors.confirmButtonColor
            )
        }
    },
    dismissButton = {
        TextButton(onClick = onDismiss) {
            Text(
                text = dismissButtonText,
                style = MaterialTheme.typography.titleMedium,
                color = colors.dismissButtonColor
            )
        }
    }
)