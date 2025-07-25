package com.kerymov.ui_rooms.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kerymov.ui_common_speedcubing.models.EventUi
import com.kerymov.ui_core.components.Divider
import com.kerymov.ui_rooms.models.SettingsUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomsCreatingBottomSheet(
    sheetState: SheetState,
    onDismissRequest:() -> Unit,
    onCreateClick: (name: String, password: String?, settings: SettingsUi) -> Unit,
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = WindowInsets(0, 0, 0, 0),
) = ModalBottomSheet(
    sheetState = sheetState,
    onDismissRequest = onDismissRequest,
    dragHandle = { BottomSheetDefaults.DragHandle(color = MaterialTheme.colorScheme.primary) },
    containerColor = MaterialTheme.colorScheme.background,
    windowInsets = windowInsets,
    modifier = modifier
) {
    var roomName by rememberSaveable { mutableStateOf("") }
    var isRoomNameError by rememberSaveable { mutableStateOf(false) }
    var event by rememberSaveable { mutableStateOf(EventUi.THREE_BY_THREE) }
    var isRoomLocked by rememberSaveable { mutableStateOf(false) }
    var roomPassword by rememberSaveable { mutableStateOf("") }
    var isPasswordError by rememberSaveable { mutableStateOf(false) }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Create a new room",
                style = MaterialTheme.typography.titleLarge,
                color = contentColorFor(MaterialTheme.colorScheme.background),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            TextButton(
                onClick = {
                    if (roomName.isBlank()) isRoomNameError = true
                    if (isRoomLocked && roomPassword.isBlank()) isPasswordError = true
                    if (isRoomNameError || isPasswordError) return@TextButton

                    val password = if (isRoomLocked) roomPassword else null
                    val settings = SettingsUi(
                        event = event,
                        isOpen = !isRoomLocked
                    )
                    onCreateClick(roomName, password, settings)
                }
            ) {
                Text(
                    text = "Create",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Icon(
            imageVector = ImageVector.vectorResource(event.icon),
            contentDescription = "Selected event",
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(20.dp)
                .size(72.dp)
        )

        OutlinedTextField(
            value = roomName,
            onValueChange = {
                roomName = it
                isRoomNameError = false
            },
            textStyle = MaterialTheme.typography.bodyLarge,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                cursorColor = MaterialTheme.colorScheme.onBackground,
                focusedPlaceholderColor = MaterialTheme.colorScheme.outlineVariant,
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.outlineVariant,
                errorBorderColor = MaterialTheme.colorScheme.error
            ),
            shape = RoundedCornerShape(8.dp),
            placeholder = {
                Text(
                    text = "Room name",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            },
            singleLine = true,
            isError = isRoomNameError,
            modifier = Modifier.fillMaxWidth()
        )

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text(
                text = "Room event",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = event.shortName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(EventUi.entries) {
                EventButton(
                    event = it,
                    onClick = { event = it },
                    isSelected = event == it,
                    modifier = Modifier.size(56.dp)
                )
            }
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 12.dp)
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp)
        ) {
            Text(
                text = "Lock the room",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Switch(
                checked = isRoomLocked,
                onCheckedChange = { isRoomLocked = !isRoomLocked },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                    checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                    checkedBorderColor = MaterialTheme.colorScheme.primaryContainer,
                    uncheckedThumbColor = MaterialTheme.colorScheme.outlineVariant,
                    uncheckedTrackColor = MaterialTheme.colorScheme.outline,
                    uncheckedBorderColor = MaterialTheme.colorScheme.outline
                ),
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }

        if (isRoomLocked) {
            OutlinedTextField(
                value = roomPassword,
                onValueChange = {
                    roomPassword = it
                    isPasswordError = false
                },
                textStyle = MaterialTheme.typography.bodyLarge,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                    cursorColor = MaterialTheme.colorScheme.onBackground,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.outlineVariant,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.outlineVariant,
                    focusedTrailingIconColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTrailingIconColor = MaterialTheme.colorScheme.onBackground,
                    errorBorderColor = MaterialTheme.colorScheme.error
                ),
                shape = RoundedCornerShape(8.dp),
                placeholder = {
                    Text(
                        text = "Password",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon =
                        if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    val description = if (isPasswordVisible) "Hide password" else "Show password"

                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = icon,
                            contentDescription = description
                        )
                    }
                },
                singleLine = true,
                isError = isPasswordError,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun EventButton(
    event: EventUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false
) = Box(
    contentAlignment = Alignment.Center,
    modifier = modifier
        .clip(RoundedCornerShape(8.dp))
        .background(
            if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background
        )
        .border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.primary,
            shape = RoundedCornerShape(8.dp)
        )
        .clickable { onClick() }
) {
    Icon(
        imageVector = ImageVector.vectorResource(event.icon),
        contentDescription = "Event",
        tint = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .size(48.dp)
            .padding(4.dp)
            .clip(RoundedCornerShape(4.dp))
    )
}

@Preview
@Composable
private fun EventButtonPreview() {
    EventButton(
        event = EventUi.THREE_BY_THREE,
        onClick = { },
        modifier = Modifier.size(56.dp)
    )
}

@Preview
@Composable
private fun EventButtonsGridPreview() {
    LazyVerticalGrid(
        columns = GridCells.FixedSize(56.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        itemsIndexed(EventUi.entries) { index, event ->
            EventButton(
                event = event, onClick = { }, isSelected = index == 0,
                modifier = Modifier.size(56.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun RoomCreatingSheetPreview() {
    RoomsCreatingBottomSheet(
        sheetState = rememberModalBottomSheetState(),
        onDismissRequest = { },
        onCreateClick = { _, _, _ ->},
        modifier = Modifier.fillMaxSize()
    )
}