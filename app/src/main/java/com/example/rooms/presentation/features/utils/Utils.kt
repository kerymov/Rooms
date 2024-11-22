package com.example.rooms.presentation.features.utils

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavHostController,
    factory: ViewModelProvider.Factory? = null
): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel(factory = factory)
    val parentEntry = remember(key1 = this) {
        navController.getBackStackEntry(navGraphRoute)
    }

    return viewModel(viewModelStoreOwner = parentEntry, factory = factory)
}

@Composable
fun PaddingValues.toOuterScaffoldPadding(
    layoutDirection: LayoutDirection = LayoutDirection.Ltr
) = PaddingValues(
    top = 0.dp,
    bottom = this.calculateBottomPadding(),
    start = this.calculateStartPadding(layoutDirection = layoutDirection),
    end = this.calculateEndPadding(layoutDirection = layoutDirection)
)

@Composable
fun PaddingValues.toInnerScaffoldPadding(
    layoutDirection: LayoutDirection = LayoutDirection.Ltr
) = PaddingValues(
    top = this.calculateTopPadding(),
    bottom = 0.dp,
    start = this.calculateStartPadding(layoutDirection = layoutDirection),
    end = this.calculateEndPadding(layoutDirection = layoutDirection)
)

enum class FadeSide {
    LEFT, RIGHT, BOTTOM, TOP
}

fun Modifier.fadingEdge(
    vararg sides: FadeSide,
    color: Color,
    width: Dp,
    isVisible: Boolean,
    spec: AnimationSpec<Dp>?
) = composed {
    require(width > 0.dp) { "Invalid fade width: Width must be greater than 0" }

    val animatedWidth = spec?.let {
        animateDpAsState(
            targetValue = if (isVisible) width else 0.dp,
            animationSpec = spec,
            label = "Fade width"
        ).value
    }

    drawWithContent {
        this@drawWithContent.drawContent()

        sides.forEach { side ->
            val (start, end) = this.size.getFadeOffsets(side)

            val staticWidth = if (isVisible) width.toPx() else 0f
            val widthPx = animatedWidth?.toPx() ?: staticWidth

            val fraction = when(side) {
                FadeSide.LEFT, FadeSide.RIGHT -> widthPx / this.size.width * 2
                FadeSide.BOTTOM, FadeSide.TOP -> widthPx / this.size.height * 2
            }

            drawRect(
                brush = Brush.linearGradient(
                    0f to color,
                    fraction to Color.Transparent,
                    start = start,
                    end = end
                ),
                size = this.size
            )
        }
    }
}

private fun Size.getFadeOffsets(side: FadeSide): Pair<Offset, Offset> {
    return when (side) {
        FadeSide.LEFT -> Offset.Zero to Offset(width, 0f)
        FadeSide.RIGHT -> Offset(width, 0f) to Offset.Zero
        FadeSide.BOTTOM -> Offset(0f, height) to Offset.Zero
        FadeSide.TOP -> Offset.Zero to Offset(0f, height)
    }
}

fun Modifier.rightFadingEdge(
    color: Color,
    isVisible: Boolean = true,
    width: Dp = 16.dp,
    spec: AnimationSpec<Dp>? = null
) = fadingEdge(FadeSide.RIGHT, color = color, width = width, isVisible = isVisible, spec = spec)