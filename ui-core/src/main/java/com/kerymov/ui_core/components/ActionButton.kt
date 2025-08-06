package com.kerymov.ui_core.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kerymov.ui_core.theme.RoomsTheme

@Composable
fun ActionButton(
    @DrawableRes icon: Int,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentColor: Color = MaterialTheme.colorScheme.outlineVariant,
    containerColor: Color = MaterialTheme.colorScheme.outline
) = Container(
    containerColor = containerColor,
    onClick = onClick,
    modifier = modifier
) {
    Icon(
        imageVector = ImageVector.vectorResource(icon),
        contentDescription = contentDescription,
        tint = contentColor,
        modifier = Modifier.size(32.dp)
    )
}

@Composable
fun ActionButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentColor: Color = MaterialTheme.colorScheme.outlineVariant,
    containerColor: Color = MaterialTheme.colorScheme.outline
) = Container(
    containerColor = containerColor,
    onClick = onClick,
    modifier = modifier
) {
    Icon(
        imageVector = icon,
        contentDescription = contentDescription,
        tint = contentColor,
        modifier = Modifier.size(32.dp)
    )
}

@Composable
private fun Container(
    containerColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) = IconButton(
    onClick = onClick,
    modifier = modifier
        .size(48.dp)
        .clip(RoundedCornerShape(12.dp))
        .background(containerColor)
) {
    content()
}

@Preview(showBackground = true)
@Composable
private fun ActionButtonPreview() {
    RoomsTheme {
        ActionButton(
            icon = Icons.Default.Favorite,
            contentDescription = "Favorite",
            onClick = {}
        )
    }
}