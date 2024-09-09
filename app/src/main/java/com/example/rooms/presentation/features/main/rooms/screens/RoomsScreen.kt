@file:OptIn(ExperimentalFoundationApi::class)

package com.example.rooms.presentation.features.main.rooms.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rooms.R
import com.example.rooms.presentation.components.CenterAlignedTopBar
import com.example.rooms.presentation.features.main.rooms.models.Event
import com.example.rooms.presentation.features.main.rooms.viewModels.RoomsUiState
import com.example.rooms.presentation.features.main.rooms.viewModels.RoomsViewModel
import com.example.rooms.presentation.features.utils.toInnerScaffoldPadding
import com.example.rooms.presentation.theme.RoomsTheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomsScreen(
    modifier: Modifier = Modifier,
    roomsViewModel: RoomsViewModel = viewModel(),
) {
    var isCreateRoomSheetOpen by rememberSaveable { mutableStateOf(false) }
    val createRoomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var roomIdToDelete by rememberSaveable { mutableStateOf<String?>(null) }
    var isDeleteRoomSheetOpen by rememberSaveable { mutableStateOf(false) }
    val deleteRoomSheetState = rememberModalBottomSheetState()

    val roomsUiState by roomsViewModel.uiState.collectAsState()

    LaunchedEffect(key1 = true, key2 = isCreateRoomSheetOpen, key3 = isDeleteRoomSheetOpen) {
        delay(2000)
        roomsViewModel.getRooms()
    }

    val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        topBar = {
            CenterAlignedTopBar(
                title = "Rooms",
                actions = listOf(
                    Icons.Filled.Add to { isCreateRoomSheetOpen = true }
                ),
                scrollBehaviour = topAppBarScrollBehavior
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = contentColorFor(MaterialTheme.colorScheme.background),
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
    ) { contentPadding ->
        Content(
            uiState = roomsUiState,
            contentPadding = contentPadding.toInnerScaffoldPadding(),
            onItemClick = { itemId ->
//                navController.navigate(NavModule.Rooms.Room.name + "/${item.id}")
            },
            onLongItemClick = { itemId ->
                isDeleteRoomSheetOpen = true
                roomIdToDelete = itemId
            }
        )

        if (isCreateRoomSheetOpen) {
            RoomsCreatingBottomSheet(
                sheetState = createRoomSheetState,
                onDismissRequest = { isCreateRoomSheetOpen = false },
                onCreateClick = { roomCreationRequest ->
                    roomsViewModel.createRoom(roomCreationRequest)
                    isCreateRoomSheetOpen = false
                },
                modifier = Modifier.fillMaxSize(),
                windowInsets = WindowInsets(0, 0, 0, 0)
            )
        }

        if (isDeleteRoomSheetOpen && roomIdToDelete != null) {
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
                windowInsets = WindowInsets(0, 0, 0, 0),
            )
        }
    }
}

@Composable
private fun Content(
    uiState: RoomsUiState,
    contentPadding: PaddingValues,
    onItemClick: (id: String) -> Unit,
    onLongItemClick: (id: String) -> Unit
) = Box(
    modifier = Modifier
        .fillMaxSize()
        .padding(contentPadding)
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(148.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(uiState.rooms) { item ->
            val event = Event.entries.find { event -> event.id == item.puzzle }
                ?: Event.THREE_BY_THREE

            RoomCard(
                name = item.roomName,
                event = event,
                isOpen = item.isOpen,
                modifier = Modifier
                    .padding(4.dp)
                    .combinedClickable(
                        onClick = { onItemClick(item.id) },
                        onLongClick = { onLongItemClick(item.id) }
                    )
            )
        }
    }
}

@Composable
private fun RoomCard(
    name: String,
    event: Event,
    isOpen: Boolean,
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

        if (!isOpen) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.lock),
                contentDescription = "Lock",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(8.dp)
                    .size(24.dp)
                    .align(Alignment.TopEnd)
            )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DeleteRoomModalBottomSheet(
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    onDeleteClick: () -> Unit,
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
        TextButton(onClick = { onDeleteClick() }) {
            Text(
                text = "Delete room",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.error,
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
            event = Event.THREE_BY_THREE,
            isOpen = true
        )
    }
}

@Preview(name = "Locked room")
@Composable
fun LockedRoomCardPreview() {
    RoomsTheme {
        RoomCard(
            name = "Room name",
            event = Event.THREE_BY_THREE,
            isOpen = false
        )
    }
}