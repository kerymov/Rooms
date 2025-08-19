package com.kerymov.ui_core.components

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Divider(
    orientation: Orientation,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(100),
    thickness: Dp = 1.dp,
    color: Color = MaterialTheme.colorScheme.outline,
) = when (orientation) {
    Orientation.Horizontal -> HorizontalDivider(
        modifier = modifier.clip(shape),
        thickness = thickness,
        color = color
    )
    Orientation.Vertical -> VerticalDivider(
        modifier = modifier.clip(shape),
        thickness = thickness,
        color = color
    )
}
