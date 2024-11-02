package com.example.rooms.presentation.features.main.rooms.models

import androidx.annotation.DrawableRes
import com.example.rooms.R

enum class EventUi(val id: Int, val shortName: String, @DrawableRes val icon: Int) {
    THREE_BY_THREE(3, "3x3", R.drawable.three),
    TWO_BY_TWO(2, "2x2", R.drawable.two),
    FOUR_BY_FOUR(4, "4x4", R.drawable.four),
    FIVE_BY_FIVE(5, "5x5", R.drawable.five),
    SIX_BY_SIX(6, "6x6", R.drawable.six),
    SEVEN_BY_SEVEN(7, "7x7", R.drawable.seven)
}