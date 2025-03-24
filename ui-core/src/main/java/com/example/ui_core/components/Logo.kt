package com.example.ui_core.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.ui_core.R
import kotlin.math.ceil

@Composable
fun Logo(modifier: Modifier = Modifier) {
    val offsetX = 0.dp
    val offsetY = 0.dp
    val blurRadius = 32.dp

    val density = LocalDensity.current
    val offsetXPx = with(density) { offsetX.toPx() }.toInt()
    val offsetYPx = with(density) { offsetY.toPx() }.toInt()
    val blurRadiusPx = ceil(with(density) { blurRadius.toPx() }).toInt()

    val color = MaterialTheme.colorScheme.primary

    val shadowModifier = Modifier
        .drawWithContent {
            val matrix = shadowColorMatrix(color)
            val filter = ColorFilter.colorMatrix(matrix)
            val paint = Paint().apply {
                colorFilter = filter
            }
            drawIntoCanvas { canvas ->
                canvas.saveLayer(Rect(0f, 0f, size.width, size.height), paint)
                drawContent()
                canvas.restore()
            }
        }
        .blur(radius = blurRadius, BlurredEdgeTreatment.Unbounded)
        .padding(all = blurRadius) // Pad to prevent clipping blur

    Layout(modifier = modifier, content = {
        Content()
        Box(modifier = shadowModifier) { Content() }
    }) { measurables, constraints ->
        val contentPlaceable = measurables[0].measure(constraints)
        val shadowPlaceable = measurables[1].measure(constraints.copy(maxWidth = contentPlaceable.width + blurRadiusPx * 2, maxHeight = contentPlaceable.height + blurRadiusPx * 2))
        layout(width = contentPlaceable.width, height = contentPlaceable.height) {
            shadowPlaceable.placeRelative(x = offsetXPx - blurRadiusPx, y = offsetYPx - blurRadiusPx)
            contentPlaceable.placeRelative(x = 0, y = 0)
        }
    }
}

@Composable
private fun Content() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Rooms",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1,
            modifier = Modifier.padding(end = 12.dp)
        )

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.logo),
            contentDescription = "Logo",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(64.dp)
        )
    }
}

private fun shadowColorMatrix(color: Color): ColorMatrix {
    return ColorMatrix().apply {
        set(0, 0, 0f) // Do not preserve original R
        set(1, 1, 0f) // Do not preserve original G
        set(2, 2, 0f) // Do not preserve original B

        set(0, 4, color.red * 255) // Use given color's R
        set(1, 4, color.green * 255) // Use given color's G
        set(2, 4, color.blue * 255) // Use given color's B
        set(3, 3, color.alpha) // Multiply by given color's alpha
    }
}