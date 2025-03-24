package com.example.ui_core.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun CircularLoadingIndicator() {
    Dialog(
        onDismissRequest = { },
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(56.dp),
            color = MaterialTheme.colorScheme.background,
            trackColor = MaterialTheme.colorScheme.primary,
            strokeCap = StrokeCap.Round,
        )
    }
}