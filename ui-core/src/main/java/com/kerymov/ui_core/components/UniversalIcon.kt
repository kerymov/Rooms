package com.kerymov.ui_core.components

import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.kerymov.ui_core.utils.IconSource

@Composable
fun UniversalIcon(
    iconSource: IconSource,
    contentDescription: String,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current
) {
    val imageVector = when (iconSource) {
        is IconSource.Vector -> iconSource.imageVector
        is IconSource.Resource -> ImageVector.vectorResource(iconSource.drawableRes)
    }
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        tint = tint,
        modifier = modifier
    )
}