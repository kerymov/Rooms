package com.example.rooms.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import com.example.rooms.R
import com.example.rooms.presentation.utils.Penalty
import com.example.rooms.presentation.theme.ChangeSystemBarsColors

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClickableTimer(
    formattedTime: String,
    isActive: Boolean,
    isEnabled: Boolean,
    penalty: Penalty,
    onStartClick: () -> Unit,
    onStopClick: () -> Unit,
    onSendResultClick: () -> Unit,
    onPlusTwoClick: () -> Unit,
    onDnfClick: () -> Unit,
    modifier: Modifier
) {
    if (isActive) {
        ChangeSystemBarsColors(
            systemBarColor = MaterialTheme.colorScheme.primary.toArgb(),
            navigationBarColor = MaterialTheme.colorScheme.primary.toArgb(),
            isAppearanceLightStatusBars = false,
            isAppearanceLightNavigationBars = false
        )
    } else {
        ChangeSystemBarsColors(
            systemBarColor = MaterialTheme.colorScheme.background.toArgb(),
            isAppearanceLightStatusBars = true,
            navigationBarColor = MaterialTheme.colorScheme.primary.toArgb(),
            isAppearanceLightNavigationBars = false
        )
    }

    var isActionButtonsVisible by rememberSaveable { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.combinedClickable(
            onClick = { onStartClick() },
            role = Role.Button,
            enabled = isEnabled,
            interactionSource = MutableInteractionSource(),
            indication = null
        )
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
                color = MaterialTheme.colorScheme.onPrimary,
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
                    icon = Icons.Rounded.Send,
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
                    onStopClick()
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