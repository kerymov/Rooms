package com.example.rooms.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.rooms.presentation.theme.RoomsTheme

@Composable
fun RoomLoginDialog(
    title: String,
    password: String,
    onPasswordChange: (String) -> Unit,
    onCancelClick: () -> Unit,
    onLoginClick: (password: String) -> Unit,
    isError: Boolean = false
) {
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onCancelClick,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = true
        )
    ) {
        Content(
            title = title,
            password = password,
            onPasswordChange = { onPasswordChange(it) },
            isPasswordVisible = isPasswordVisible,
            onPasswordVisibilityChange = { isPasswordVisible = !isPasswordVisible },
            isError = isError,
            onCancelClick = onCancelClick,
            onLoginClick = { onLoginClick(it) }
        )
    }
}

@Composable
private fun Content(
    title: String,
    password: String,
    onPasswordChange: (String) -> Unit,
    isPasswordVisible: Boolean,
    onPasswordVisibilityChange: () -> Unit,
    isError: Boolean,
    onCancelClick: () -> Unit,
    onLoginClick: (password: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(
                horizontal = 12.dp,
                vertical = 24.dp
            )
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(20.dp))
        PasswordTextField(
            value = password,
            onValueChange = { onPasswordChange(it) },
            isValueVisible = isPasswordVisible,
            onValueVisibilityChange = onPasswordVisibilityChange,
            placeholderText = "Password",
            isError = isError,
            imeAction = ImeAction.Done
        )
        Spacer(Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
            modifier = Modifier.fillMaxWidth()
        ) {
            TextButton(onClick = onCancelClick) {
                Text(
                    text = "Cancel",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            TextButton(onClick = { onLoginClick(password) }) {
                Text(
                    text = "Login",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview(device = Devices.PIXEL, showSystemUi = true, showBackground = true)
@Composable
private fun LoginPopupPreview() {
    RoomsTheme {
        Content(
            title = "Room name",
            password = "",
            onPasswordChange = {},
            isPasswordVisible = false,
            onPasswordVisibilityChange = { },
            isError = false,
            onCancelClick = {},
            onLoginClick = {}
        )
    }
}