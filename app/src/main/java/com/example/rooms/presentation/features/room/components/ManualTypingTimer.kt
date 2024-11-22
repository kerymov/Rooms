package com.example.rooms.presentation.features.room.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.rooms.R
import com.example.rooms.presentation.features.room.utils.MAX_TIME_LENGTH
import com.example.rooms.presentation.features.room.utils.TimerVisualTransformation

@Composable
fun ManualTypingTimer(
    modifier: Modifier,
    onSendResultClick: () -> Unit
) = Column(
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = modifier
){
    var time by rememberSaveable { mutableStateOf("") }

    OutlinedTextField(
        value = time,
        onValueChange = { },
        placeholder = {
            Text(
                text = "0.00",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.secondaryContainer,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        leadingIcon = {
            IconButton(onClick = {}, enabled = false) { Box(modifier = Modifier.size(32.dp)) }
        },
        trailingIcon = {
            IconButton(onClick = {
                time = ""
                onSendResultClick()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.Send,
                    contentDescription = "Send",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        visualTransformation = TimerVisualTransformation(),
        colors = TextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.onPrimary,
            disabledTrailingIconColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        readOnly = true,
        enabled = false,
        singleLine = true,
        textStyle = MaterialTheme.typography.displayMedium.copy(textAlign = TextAlign.Center),
        modifier = Modifier.fillMaxWidth()
    )

    NumberKeyboard(
        onButtonClick = { if (time.length < MAX_TIME_LENGTH) time += it },
        onBackspaceClick = { time = time.dropLast(1) }
    )
}

@Composable
private fun NumberKeyboard(
    onButtonClick: (String) -> Unit,
    onBackspaceClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            KeyboardNumberButton(text = "1", onClick = onButtonClick)
            KeyboardNumberButton(text = "2", onClick = onButtonClick)
            KeyboardNumberButton(text = "3", onClick = onButtonClick)
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            KeyboardNumberButton(text = "4", onClick = onButtonClick)
            KeyboardNumberButton(text = "5", onClick = onButtonClick)
            KeyboardNumberButton(text = "6", onClick = onButtonClick)
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            KeyboardNumberButton(text = "7", onClick = onButtonClick)
            KeyboardNumberButton(text = "8", onClick = onButtonClick)
            KeyboardNumberButton(text = "9", onClick = onButtonClick)
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(56.dp))
            KeyboardNumberButton(text = "0", onClick = onButtonClick)
            KeyboardIconButton(icon = R.drawable.backspace, onClick = onBackspaceClick)
        }
    }
}

@Composable
private fun KeyboardNumberButton(
    text: String,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) = TextButton(
    onClick = { onClick(text) },
    shape = RoundedCornerShape(100),
    contentPadding = PaddingValues(12.dp),
    colors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.surfaceTint,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ),
    modifier = modifier.size(64.dp)
) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineSmall
    )
}

@Composable
private fun KeyboardIconButton(
    @DrawableRes icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) = IconButton(
    onClick = onClick,
    modifier = modifier.size(64.dp)
) {
    Icon(
        imageVector = ImageVector.vectorResource(icon),
        contentDescription = "Backspace",
        tint = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier.size(36.dp)
    )
}