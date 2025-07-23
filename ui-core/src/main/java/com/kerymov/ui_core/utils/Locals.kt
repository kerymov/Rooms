package com.kerymov.ui_core.utils

import androidx.compose.runtime.staticCompositionLocalOf
import com.kerymov.ui_core.models.UserUi

val LocalUser = staticCompositionLocalOf<UserUi?> { null }