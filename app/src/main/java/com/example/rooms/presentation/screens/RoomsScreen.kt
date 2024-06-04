package com.example.rooms.presentation.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rooms.R
import com.example.rooms.data.remote.rooms.models.Room
import com.example.rooms.presentation.components.TopBar
import com.example.rooms.presentation.uiModels.Event
import com.example.rooms.presentation.viewModels.RoomsViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun RoomsScreen(
    modifier: Modifier = Modifier,
    roomsViewModel: RoomsViewModel = viewModel(),
    onRoomCardClick: (room: Room) -> Unit,
) {
    var isSheetOpen by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val roomsUiState by roomsViewModel.uiState.collectAsState()

    LaunchedEffect(key1 = true, key2 = isSheetOpen) {
        delay(2000)
        roomsViewModel.getRooms()
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "Rooms",
                navigationIcon = null,
                actionIcon = R.drawable.add,
                onNavigationButtonClick = { },
                onActionButtonClick = {
                    isSheetOpen = true
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = contentColorFor(MaterialTheme.colorScheme.background),
        modifier = modifier.fillMaxSize()
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(148.dp),
            contentPadding = PaddingValues(
                top = it.calculateTopPadding() + 8.dp,
                bottom = it.calculateBottomPadding() + 0.dp,
                start = it.calculateStartPadding(LayoutDirection.Ltr) + 16.dp,
                end = it.calculateEndPadding(LayoutDirection.Ltr) + 16.dp
            ),
            modifier = Modifier.fillMaxSize()
        ) {
            items(roomsUiState.rooms) { item ->
                val event = Event.entries.find { event -> event.id == item.puzzle }
                    ?: Event.THREE_BY_THREE

                RoomCard(
                    name = item.roomName,
                    event = event,
                    isOpen = item.isOpen,
                    modifier = Modifier
                        .padding(4.dp)
                        .combinedClickable(
                            onClick = { onRoomCardClick(item) },
                            onLongClick = {  }
                        )
                )
            }
        }

        if (isSheetOpen) {
            RoomsCreatingBottomSheet(
                sheetState = sheetState,
                onDismissRequest = { isSheetOpen = false },
                onCreateClick = { roomCreationRequest ->
                    roomsViewModel.createRoom(roomCreationRequest)
                    isSheetOpen = false
                },
                modifier = Modifier.fillMaxSize()
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

@Preview(name = "Open room")
@Composable
fun OpenRoomCardPreview() {
    RoomCard(
        name = "Room name",
        event = Event.THREE_BY_THREE,
        isOpen = true
    )
}

@Preview(name = "Locked room")
@Composable
fun LockedRoomCardPreview() {
    RoomCard(
        name = "Room name",
        event = Event.THREE_BY_THREE,
        isOpen = false
    )
}