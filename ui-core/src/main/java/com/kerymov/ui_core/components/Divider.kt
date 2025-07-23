package com.kerymov.ui_core.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun Divider(
    modifier: Modifier = Modifier
) = HorizontalDivider(
    modifier = modifier.clip(RoundedCornerShape(100)),
    thickness = 1.dp,
    color = MaterialTheme.colorScheme.outline
)