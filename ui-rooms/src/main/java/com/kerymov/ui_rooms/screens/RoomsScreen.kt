@file:OptIn(ExperimentalFoundationApi::class)

package com.kerymov.ui_rooms.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.LockOpen
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kerymov.ui_common_speedcubing.models.EventUi
import com.kerymov.ui_core.components.CircularLoadingIndicator
import com.kerymov.ui_core.components.ErrorCard
import com.kerymov.ui_core.theme.RoomsTheme
import com.kerymov.ui_core.utils.LocalUser
import com.kerymov.ui_core.utils.defaultBottomSheetPadding
import com.kerymov.ui_rooms.components.RoomLoginDialog
import com.kerymov.ui_rooms.components.RoomsCreatingBottomSheet
import com.kerymov.ui_rooms.models.RoomDetailsUi
import com.kerymov.ui_rooms.models.RoomUi
import com.kerymov.ui_rooms.viewModels.LoadingState
import com.kerymov.ui_rooms.viewModels.RoomsStatus
import com.kerymov.ui_rooms.viewModels.RoomsViewModel
import kotlinx.serialization.json.Json

private enum class RoomAccessStatus {
    OPEN, LOCKED, UNLOCKED
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun RoomsScreen(
    modifier: Modifier = Modifier,
    onRoomLogin: (roomDetails: String) -> Unit,
    roomsViewModel: RoomsViewModel,
) {
    val createRoomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var roomNameToLogin by rememberSaveable { mutableStateOf("") }
    var roomPasswordToLogin by rememberSaveable { mutableStateOf("") }
    var isLoginError by rememberSaveable { mutableStateOf(false) }
    var shouldShowLoginDialog by rememberSaveable { mutableStateOf(false) }

    var roomIdToDelete by rememberSaveable { mutableStateOf<String?>(null) }
    var isDeleteRoomSheetOpen by rememberSaveable { mutableStateOf(false) }
    val deleteRoomSheetState = rememberModalBottomSheetState()

    val roomsUiState by roomsViewModel.uiState.collectAsState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = roomsUiState.loadingState == LoadingState.REFRESHING,
        onRefresh = { roomsViewModel.getRooms(isRefreshing = true) }
    )

    LaunchedEffect(key1 = roomsUiState.currentRoomDetails) {
        val roomDetails = roomsUiState.currentRoomDetails
        if (roomDetails != null) {
            val roomDetailsJson = Json.encodeToString(
                serializer = RoomDetailsUi.serializer(),
                value = roomDetails
            )
            onRoomLogin(roomDetailsJson)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        when (val roomsStatus = roomsUiState.roomsStatus) {
            is RoomsStatus.Success -> {
                Content(
                    rooms = roomsStatus.rooms.reversed(),
                    onItemClick = { name, isOpen ->
                        if (isOpen) {
                            roomsViewModel.loginRoom(name, null)
                        } else {
                            roomNameToLogin = name
                            shouldShowLoginDialog = true
                        }
                    },
                    onLongItemClick = { itemId ->
                        isDeleteRoomSheetOpen = true
                        roomIdToDelete = itemId
                    },
                    onCreateNewRoomClick = { roomsViewModel.toggleCreateRoomBottomSheet(isOpen = true) }
                )
            }
            is RoomsStatus.Failure -> {
                ErrorView(
                    errorMessage = "${roomsUiState.error?.code}: " + roomsUiState.error?.message,
                    onTryAgainClick = { roomsViewModel.getRooms() }
                )
            }
            RoomsStatus.None -> Unit
        }

        if (roomsUiState.loadingState == LoadingState.LOADING) {
            CircularLoadingIndicator()
        }

        PullRefreshIndicator(
            refreshing = roomsUiState.loadingState == LoadingState.REFRESHING,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            contentColor = MaterialTheme.colorScheme.primary,
            backgroundColor = MaterialTheme.colorScheme.surface
        )

        if (roomsUiState.error != null) {
            ErrorCard(
                text = roomsUiState.error?.message.toString(),
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }

    if (roomsUiState.isCreateRoomBottomSheetOpen) {
        RoomsCreatingBottomSheet(
            sheetState = createRoomSheetState,
            onDismissRequest = { roomsViewModel.toggleCreateRoomBottomSheet(isOpen = false) },
            onCreateClick = { roomName, roomPassword, roomSettings ->
                roomsViewModel.createRoom(roomName, roomPassword, roomSettings)
                roomsViewModel.toggleCreateRoomBottomSheet(isOpen = false)
            },
            modifier = Modifier
                .fillMaxSize()
                .defaultBottomSheetPadding()
        )
    }

    if (shouldShowLoginDialog) {
        RoomLoginDialog(
            title = roomNameToLogin,
            password = roomPasswordToLogin,
            onPasswordChange = { roomPasswordToLogin = it },
            onCancelClick = {
                roomNameToLogin = ""
                roomPasswordToLogin = ""
                isLoginError = false
                shouldShowLoginDialog = false
            },
            onLoginClick = {
                shouldShowLoginDialog = false
                roomsViewModel.loginRoom(name = roomNameToLogin, password = roomPasswordToLogin)
            }
        )
    }

    if (isDeleteRoomSheetOpen) {
        val localUser = LocalUser.current
        val roomToDelete = (roomsUiState.roomsStatus as? RoomsStatus.Success)?.rooms?.find { it.id == roomIdToDelete }
        val isAdministrator = roomToDelete?.administratorName == localUser?.username

        DeleteRoomModalBottomSheet(
            sheetState = deleteRoomSheetState,
            onDismissRequest = {
                isDeleteRoomSheetOpen = false
                roomIdToDelete = null
            },
            onDeleteClick = {
                roomsViewModel.deleteRoom(roomIdToDelete ?: "")
                isDeleteRoomSheetOpen = false
                roomIdToDelete = null
            },
            isDeleteButtonEnabled = isAdministrator,
            windowInsets = WindowInsets(0, 0, 0, 0),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun Content(
    rooms: List<RoomUi>,
    onItemClick: (name: String, isOpen: Boolean) -> Unit,
    onLongItemClick: (id: String) -> Unit,
    onCreateNewRoomClick: () -> Unit
) = Box(
    modifier = Modifier.fillMaxSize()
) {
    if (rooms.isEmpty()) {
        NoRoomsView(
            onCreateNewRoomClick = onCreateNewRoomClick
        )
    } else {
        RoomsGrid(
            rooms = rooms,
            onItemClick = onItemClick,
            onLongItemClick = onLongItemClick
        )
    }
}

@Composable
private fun RoomsGrid(
    rooms: List<RoomUi>,
    onItemClick: (name: String, isOpen: Boolean) -> Unit,
    onLongItemClick: (id: String) -> Unit
) = LazyVerticalGrid(
    columns = GridCells.Adaptive(148.dp),
    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
    modifier = Modifier.fillMaxSize()
) {
    items(rooms) { item ->
        val event = EventUi.entries.find { event -> event.id == item.event.id }
            ?: EventUi.THREE_BY_THREE
        val accessStatus = when {
            item.isOpen -> RoomAccessStatus.OPEN
            item.administratorName == LocalUser.current?.username -> RoomAccessStatus.UNLOCKED
            else -> RoomAccessStatus.LOCKED
        }

        RoomCard(
            name = item.name,
            event = event,
            accessStatus = accessStatus,
            onClick = {
                onItemClick(item.name, accessStatus != RoomAccessStatus.LOCKED)
            },
            onLongClick = { onLongItemClick(item.id) },
            modifier = Modifier.padding(4.dp)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun RoomCard(
    name: String,
    event: EventUi,
    accessStatus: RoomAccessStatus,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) = Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = modifier
        .clip(RoundedCornerShape(16.dp))
        .background(MaterialTheme.colorScheme.background)
        .border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.primary,
            shape = RoundedCornerShape(16.dp)
        )
        .width(148.dp)
        .combinedClickable(
            onClick = onClick,
            onLongClick = onLongClick
        )
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Icon(
            imageVector = ImageVector.vectorResource(id = event.icon),
            contentDescription = "Event icon",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(24.dp)
                .size(72.dp)
                .align(Alignment.Center)
        )

        val accessIconModifier =  Modifier
            .padding(8.dp)
            .size(24.dp)
            .align(Alignment.TopEnd)

        when (accessStatus) {
            RoomAccessStatus.LOCKED -> {
                Icon(
                    imageVector = Icons.Rounded.Lock,
                    contentDescription = "Locked",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = accessIconModifier
                )
            }
            RoomAccessStatus.UNLOCKED -> {
                Icon(
                    imageVector = Icons.Rounded.LockOpen,
                    contentDescription = "Unlocked",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = accessIconModifier
                )
            }
            RoomAccessStatus.OPEN -> Unit
        }
    }

    Text(
        text = name,
        textAlign = TextAlign.Center,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.titleSmall,
        color = contentColorFor(MaterialTheme.colorScheme.primary),
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(vertical = 8.dp, horizontal = 12.dp)
    )
}

@Composable
private fun NoRoomsView(
    onCreateNewRoomClick: () -> Unit
) = Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
    modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 20.dp, vertical = 12.dp)
) {
    Text(
        text = "No rooms, create a new one",
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onBackground
    )
    Spacer(Modifier.size(36.dp))
    Button(
        contentPadding = PaddingValues(vertical = 12.dp, horizontal = 48.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        onClick = onCreateNewRoomClick,
        modifier = Modifier
            .widthIn(184.dp)
            .heightIn(min = 48.dp)
    ) {
        Text(
            text = "Create room",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun ErrorView(
    errorMessage: String,
    onTryAgainClick: () -> Unit
) = Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
    modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 20.dp, vertical = 12.dp)
) {
    Text(
        text = "Error",
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.error
    )
    Spacer(Modifier.size(16.dp))
    Text(
        text = errorMessage,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.error
    )
    Spacer(Modifier.size(36.dp))
    Button(
        contentPadding = PaddingValues(vertical = 12.dp, horizontal = 48.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        onClick = onTryAgainClick,
        modifier = Modifier
            .widthIn(184.dp)
            .heightIn(min = 48.dp)
    ) {
        Text(
            text = "Try again",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DeleteRoomModalBottomSheet(
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    onDeleteClick: () -> Unit,
    isDeleteButtonEnabled: Boolean,
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = BottomSheetDefaults.windowInsets
) = ModalBottomSheet(
    sheetState = sheetState,
    onDismissRequest = onDismissRequest,
    containerColor = MaterialTheme.colorScheme.background,
    dragHandle = { BottomSheetDefaults.DragHandle(color = MaterialTheme.colorScheme.primary) },
    modifier = modifier,
    windowInsets = windowInsets,
) {
    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
            )
            .fillMaxWidth()
            .padding(4.dp)
            .padding(bottom = 32.dp)
    ) {
        TextButton(
            onClick = { onDeleteClick() },
            enabled = isDeleteButtonEnabled,
            colors = ButtonDefaults.textButtonColors(
                contentColor = MaterialTheme.colorScheme.error,
                disabledContentColor = MaterialTheme.colorScheme.outlineVariant
            )
        ) {
            Text(
                text = "Delete room",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(name = "Open room")
@Composable
fun OpenRoomCardPreview() {
    RoomsTheme {
        RoomCard(
            name = "Room name",
            event = EventUi.THREE_BY_THREE,
            accessStatus = RoomAccessStatus.OPEN,
            onClick = { },
            onLongClick = { }
        )
    }
}

@Preview(name = "Locked room")
@Composable
fun LockedRoomCardPreview() {
    RoomsTheme {
        RoomCard(
            name = "Room name",
            event = EventUi.THREE_BY_THREE,
            accessStatus = RoomAccessStatus.LOCKED,
            onClick = { },
            onLongClick = { }
        )
    }
}

@Preview(name = "Unlocked room")
@Composable
fun UnlockedRoomCardPreview() {
    RoomsTheme {
        RoomCard(
            name = "Room name",
            event = EventUi.THREE_BY_THREE,
            accessStatus = RoomAccessStatus.UNLOCKED,
            onClick = { },
            onLongClick = { }
        )
    }
}