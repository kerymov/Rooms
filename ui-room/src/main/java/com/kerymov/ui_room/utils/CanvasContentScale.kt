package com.kerymov.ui_room.utils

import androidx.compose.ui.geometry.Size

enum class CanvasContentScale {
    CENTER_FIT, CENTER_CROP,
}

fun calculateContentZoom(
    canvasSize: Size,
    contentSize: Size,
    contentScale: CanvasContentScale
): Float {
    val horizontalZoom = canvasSize.width / contentSize.width
    val verticalZoom = canvasSize.height / contentSize.height
    return when (contentScale) {
        CanvasContentScale.CENTER_FIT -> minOf(horizontalZoom, verticalZoom)
        CanvasContentScale.CENTER_CROP -> maxOf(horizontalZoom, verticalZoom)
    }
}