package com.kerymov.ui_core.screens

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 *
 */
@Composable
fun ContainerLayout(
    topAppBar: @Composable () -> Unit,
    content: @Composable (modifier: Modifier) -> Unit,
    modifier: Modifier = Modifier,
    contentWindowInsets: WindowInsets = WindowInsets(0.dp),
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(MaterialTheme.colorScheme.background),
) {
    Scaffold(
        topBar = { topAppBar() },
        contentWindowInsets = contentWindowInsets,
        containerColor = containerColor,
        contentColor = contentColor,
        modifier = modifier.fillMaxSize(),
    ) { contentPadding ->
        content(Modifier.padding(contentPadding))
    }
}
