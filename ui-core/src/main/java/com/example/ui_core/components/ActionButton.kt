package com.example.ui_core.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp

@Composable
fun ActionButton(
    @DrawableRes icon: Int,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false
) = IconButton(
    onClick = onClick,
    modifier = modifier
        .size(56.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(
            if (isSelected) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.surfaceTint
            }
        )
) {
    Icon(
        imageVector = ImageVector.vectorResource(icon),
        contentDescription = contentDescription,
        tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier.size(32.dp)
    )
}

@Composable
fun ActionButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false
) = IconButton(
    onClick = onClick,
    modifier = modifier
        .size(56.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(
            if (isSelected) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.surfaceTint
            }
        )
) {
    Icon(
        imageVector = icon,
        contentDescription = contentDescription,
        tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier.size(32.dp)
    )
}