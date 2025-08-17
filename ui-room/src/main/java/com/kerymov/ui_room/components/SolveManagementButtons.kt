package com.kerymov.ui_room.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kerymov.ui_common_speedcubing.models.PenaltyUi
import com.kerymov.ui_core.components.ActionButton
import com.kerymov.ui_core.components.Divider
import com.kerymov.ui_core.theme.DnfRed
import com.kerymov.ui_core.theme.DoneGreen
import com.kerymov.ui_core.theme.PlusTwoYellow
import com.kerymov.ui_core.theme.RoomsTheme
import com.kerymov.ui_core.utils.IconSource
import com.kerymov.ui_room.R

@Composable
fun SolveManagementButtons(
    penalty: PenaltyUi,
    onPenaltyChange: (PenaltyUi) -> Unit,
    onSendResultClick: () -> Unit,
    modifier: Modifier = Modifier
) = Row(
    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier
        .height(IntrinsicSize.Min)
        .clip(RoundedCornerShape(16.dp))
        .background(color = MaterialTheme.colorScheme.onPrimary)
        .padding(vertical = 8.dp, horizontal = 12.dp)
) {
    val transition = updateTransition(targetState = penalty)
    val dnfButtonColor by transition.animateColor(
        transitionSpec = { tween(durationMillis = 200) }
    ) { state ->
        if (state == PenaltyUi.DNF) DnfRed else Color.Transparent
    }

    val plusTwoButtonColor by transition.animateColor(
        transitionSpec = { tween(durationMillis = 200) }
    ) { state ->
        if (state == PenaltyUi.PLUS_TWO) PlusTwoYellow else Color.Transparent
    }

    val noPenaltyButtonColor by transition.animateColor(
        transitionSpec = { tween(durationMillis = 200) }
    ) { state ->
        if (state == PenaltyUi.NO_PENALTY) DoneGreen else Color.Transparent
    }

    ActionButton(
        iconSource = IconSource.Resource(R.drawable.did_not_finish),
        contentDescription = "DNF",
        onClick = {
            onPenaltyChange(PenaltyUi.DNF)
        },
        contentColor = if (penalty == PenaltyUi.DNF) MaterialTheme.colorScheme.onPrimary else DnfRed,
        containerColor = dnfButtonColor,
    )
    ActionButton(
        iconSource = IconSource.Resource(R.drawable.exposure_plus_2),
        contentDescription = "Plus 2",
        onClick = {
            onPenaltyChange(PenaltyUi.PLUS_TWO)
        },
        contentColor = if (penalty == PenaltyUi.PLUS_TWO) MaterialTheme.colorScheme.onPrimary else PlusTwoYellow,
        containerColor = plusTwoButtonColor,
    )

    ActionButton(
        iconSource = IconSource.Resource(R.drawable.check),
        contentDescription = "No penalty",
        onClick = {
            onPenaltyChange(PenaltyUi.NO_PENALTY)
        },
        contentColor = if (penalty == PenaltyUi.NO_PENALTY) MaterialTheme.colorScheme.onPrimary else DoneGreen,
        containerColor = noPenaltyButtonColor,
    )

    Divider(
        orientation = Orientation.Vertical,
        modifier = Modifier.fillMaxHeight()
    )

    ActionButton(
        iconSource = IconSource.Vector(Icons.AutoMirrored.Rounded.Send),
        contentDescription = "Send",
        onClick = onSendResultClick,
        contentColor = MaterialTheme.colorScheme.primary,
        containerColor = Color.Transparent,
    )
}

@Preview
@Composable
private fun SolveManagementButtonsNoPenaltyPreview() {
    RoomsTheme {
        SolveManagementButtons(
            penalty = PenaltyUi.NO_PENALTY,
            onPenaltyChange = { },
            onSendResultClick = { }
        )
    }
}

@Preview
@Composable
private fun SolveManagementButtonsPlusTwoPenaltyPreview() {
    RoomsTheme {
        SolveManagementButtons(
            penalty = PenaltyUi.PLUS_TWO,
            onPenaltyChange = { },
            onSendResultClick = { }
        )
    }
}

@Preview
@Composable
private fun SolveManagementButtonsDnfPenaltyPreview() {
    RoomsTheme {
        SolveManagementButtons(
            penalty = PenaltyUi.DNF,
            onPenaltyChange = { },
            onSendResultClick = { }
        )
    }
}