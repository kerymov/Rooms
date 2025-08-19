package com.kerymov.ui_core.utils

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.vector.ImageVector

sealed interface IconSource {
    data class Vector(val imageVector: ImageVector) : IconSource
    data class Resource(@DrawableRes val drawableRes: Int) : IconSource
}
