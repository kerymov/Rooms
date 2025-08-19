package com.kerymov.ui_core.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kerymov.ui_core.theme.RoomsTheme
import com.kerymov.ui_core.utils.IconSource

@Composable
fun ActionButton(
    iconSource: IconSource,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentColor: Color = MaterialTheme.colorScheme.outlineVariant,
    containerColor: Color = MaterialTheme.colorScheme.outline
) = Box(
    contentAlignment = Alignment.Center,
    modifier = modifier
        .size(36.dp)
        .clip(RoundedCornerShape(12.dp))
        .clickable { onClick() }
        .background(containerColor)
) {
    UniversalIcon(
        iconSource = iconSource,
        contentDescription = contentDescription,
        tint = contentColor,
        modifier = Modifier.size(24.dp)
    )
}

@Composable
fun SelectableActionButton(
    isSelected: Boolean,
    iconSource: IconSource,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentColor: Color = MaterialTheme.colorScheme.outlineVariant,
    containerColor: Color = MaterialTheme.colorScheme.outline
) {
    val backgroundColorAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = tween(durationMillis = 200)
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(36.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .drawBehind {
                drawRoundRect(
                    color = containerColor,
                    size = size,
                    cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx()),
                    alpha = backgroundColorAlpha
                )
            }
    ) {
        UniversalIcon(
            iconSource = iconSource,
            contentDescription = contentDescription,
            tint = contentColor,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectableActionButtonPreview() {
    RoomsTheme {
        SelectableActionButton(
            isSelected = true,
            iconSource = IconSource.Vector(Icons.Default.Favorite),
            contentDescription = "Favorite",
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ActionButtonPreview() {
    RoomsTheme {
        ActionButton(
            iconSource = IconSource.Vector(Icons.Default.Favorite),
            contentDescription = "Favorite",
            onClick = {}
        )
    }
}