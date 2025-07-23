package com.kerymov.ui_onboarding.utils

data class TextFieldHandler(
    val value: String,
    val onValueChange: (String) -> Unit,
    val isTextVisible: Boolean = true,
    val onVisibilityChange: () -> Unit = { },
)