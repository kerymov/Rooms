package com.example.ui_room.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import com.example.ui_core.components.ActionButton
import com.example.ui_core.theme.ChangeSystemBarsColors
import com.example.ui_room.R
import com.example.ui_room.utils.Penalty
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ClickableTimer(
    formattedTime: String,
    penalty: Penalty,
    isEnabled: Boolean,
    isActive: Boolean,
    onStart: () -> Unit,
    onDismiss: () -> Unit,
    onStop: () -> Unit,
    onSendResultClick: () -> Unit,
    onPlusTwoClick: () -> Unit,
    onDnfClick: () -> Unit,
    modifier: Modifier
) {
    if (isActive) {
        ChangeSystemBarsColors(
            systemBarColor = MaterialTheme.colorScheme.primary,
            navigationBarColor = MaterialTheme.colorScheme.primary,
            isAppearanceLightStatusBars = false,
            isAppearanceLightNavigationBars = false
        )
    } else {
        ChangeSystemBarsColors(
            systemBarColor = MaterialTheme.colorScheme.background,
            isAppearanceLightStatusBars = true,
            navigationBarColor = MaterialTheme.colorScheme.primary,
            isAppearanceLightNavigationBars = false
        )
    }

    var isActionButtonsVisible by rememberSaveable { mutableStateOf(false) }

    val defaultColor = MaterialTheme.colorScheme.onPrimary
    val preparationColor = Color.Red
    val readyColor = Color.Green
    var timerColor by remember { mutableStateOf(defaultColor) }

    var isTimerPressed by remember { mutableStateOf(false) }
    val timerPreparationCoroutineScope = rememberCoroutineScope()

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { _ ->
                        if (!isEnabled) return@detectTapGestures

                        isTimerPressed = true
                        timerColor = preparationColor
                        val pressStartTime = System.currentTimeMillis()

                        val preparationJob = timerPreparationCoroutineScope.launch {
                            delay(500)
                            if (isTimerPressed) {
                                timerColor = readyColor
                            }
                        }

                        try {
                            awaitRelease()

                            timerColor = defaultColor
                            preparationJob.cancel()

                            val pressingDuration = System.currentTimeMillis() - pressStartTime
                            if (pressingDuration > 500) {
                                onStart()
                            } else {
                                onDismiss()
                            }
                        } catch (e: Exception) {
                            onDismiss()
                            preparationJob.cancel()
                        }
                    }
                )
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 48.dp)
        ) {
            Text(
                text = if (penalty == Penalty.DNF) "DNF" else formattedTime,
                style = MaterialTheme.typography.displayLarge,
                color = timerColor,
                textAlign = TextAlign.Center
            )
        }

        if (isActionButtonsVisible) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ActionButton(
                    icon = R.drawable.did_not_finish,
                    contentDescription = "DNF",
                    isSelected = penalty == Penalty.DNF,
                    onClick = onDnfClick
                )
                ActionButton(
                    icon = R.drawable.exposure_plus_2,
                    contentDescription = "Plus 2",
                    isSelected = penalty == Penalty.PLUS_TWO,
                    onClick = onPlusTwoClick
                )
                ActionButton(
                    icon = Icons.AutoMirrored.Rounded.Send,
                    contentDescription = "Send",
                    onClick = {
                        isActionButtonsVisible = false
                        onSendResultClick()
                    }
                )
            }
        }

        if (isActive) {
            ActiveTimerOverlay(
                formattedTime = formattedTime,
                onDismissRequest = {
                    isActionButtonsVisible = true
                    onStop()
                }
            )
        }
    }
}

@Composable
private fun ActiveTimerOverlay(
    formattedTime: String,
    onDismissRequest: () -> Unit
) = Popup(
    onDismissRequest = onDismissRequest,
    popupPositionProvider = object : PopupPositionProvider {
        override fun calculatePosition(
            anchorBounds: IntRect,
            windowSize: IntSize,
            layoutDirection: LayoutDirection,
            popupContentSize: IntSize
        ): IntOffset {
            return IntOffset.Zero
        }
    }
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary)
            .clickable { onDismissRequest() },
    ) {
        Text(
            text = formattedTime,
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}