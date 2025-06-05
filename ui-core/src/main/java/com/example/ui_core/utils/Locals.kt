package com.example.ui_core.utils

import androidx.compose.runtime.staticCompositionLocalOf
import com.example.ui_core.models.UserUi

val LocalUser = staticCompositionLocalOf<UserUi?> { null }