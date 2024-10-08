@file:OptIn(ExperimentalFoundationApi::class)

package com.example.rooms.presentation.features.main.rooms.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.rooms.R
import com.example.rooms.data.model.scramble.ImageDto
import com.example.rooms.presentation.features.main.rooms.models.Event
import com.example.rooms.presentation.features.main.rooms.components.ClickableTimer
import com.example.rooms.presentation.features.main.rooms.components.ManualTypingTimer
import com.example.rooms.presentation.features.main.rooms.components.ScrambleImage
import com.example.rooms.presentation.components.CenterAlignedTopBar
import com.example.rooms.presentation.features.main.rooms.viewModels.RoomViewModel
import com.example.rooms.presentation.features.main.rooms.utils.Timer
import com.example.rooms.presentation.features.main.rooms.utils.TimerState

private enum class Page {
    SCRAMBLE,
    SCRAMBLE_IMAGE
}

private enum class TimerMode(val label: String) {
    TIMER("Timer"),
    TYPING("Typing")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    roomViewModel: RoomViewModel = viewModel(),
) {
    val roomUiState by roomViewModel.uiState.collectAsState()

    val event = Event.entries.find { it.id == roomUiState.room?.puzzle }
    Scaffold(
        topBar = {
//            CenterAlignedTopBar(
//                title = "${roomUiState.room?.roomName} - ${event?.shortName}",
//                navigationIcon = R.drawable.exit_to_app,
//                actionIcon = R.drawable.groups,
//                onNavigationButtonClick = { navController.popBackStack() },
//                onActionButtonClick = {
////                    navController.navigate(NavModule.Rooms.Results.name + "/${roomUiState.room?.id}")
//                }
//            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = contentColorFor(MaterialTheme.colorScheme.background),
        modifier = modifier.fillMaxSize()
    ) {
        var timerMode by rememberSaveable { mutableStateOf(TimerMode.TIMER) }
        var isSheetOpen by rememberSaveable { mutableStateOf(false) }
        val sheetState = rememberModalBottomSheetState()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            val scramble = roomUiState.scramble?.scramble ?: ""
            val image = roomUiState.scramble?.image ?: ImageDto(listOf())
            ScrambleZone(scramble, image)
            TimerZone(
                timerMode = timerMode,
                isSelectModeMenuShown = isSheetOpen,
                onChangeTimerModeClick = { isSheetOpen = !isSheetOpen },
                onSendResultClick = { resultInMills ->
                    roomViewModel.getScramble()
//                    SolvesRepository.addSolve(
//                        Solve(
//                            username = signInViewModel.uiState.value.username ?: "",
//                            roomName = roomViewModel.uiState.value.room?.roomName ?: "",
//                            resultInMills = resultInMills
//                        )
//                    )
                },
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            )
        }

        if (isSheetOpen) {
            TimerModeModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = { isSheetOpen = false },
                onItemSelect = { mode ->
                    timerMode = mode
                    isSheetOpen = false
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ScrambleZone(
    scramble: String,
    image: ImageDto
) {
    val pagerState = rememberPagerState(
        initialPage = Page.SCRAMBLE.ordinal,
        initialPageOffsetFraction = 0f
    ) { 2 }

    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(vertical = 12.dp, horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        key = { Page.entries[it].ordinal },
        pageSize = PageSize.Fixed(300.dp)
    ) { page ->
        when (page) {
            Page.SCRAMBLE.ordinal -> {
                Text(
                    text = scramble,
                    style = MaterialTheme.typography.bodyMedium,
                    color = contentColorFor(backgroundColor = MaterialTheme.colorScheme.background),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp)
                )
            }
            Page.SCRAMBLE_IMAGE.ordinal -> {
                ScrambleImage(
                    image = image,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 20.dp)
    ) {
        items(Page.entries) { page ->
            PageIndicator(
                isActive = page.ordinal == pagerState.targetPage,
                modifier = Modifier.padding(end = if (page.ordinal == Page.entries.lastIndex) 0.dp else 8.dp)
            )
        }
    }
}

@Composable
private fun PageIndicator(
    isActive: Boolean,
    modifier: Modifier = Modifier
) = Box(
    modifier = modifier
        .clip(RoundedCornerShape(100))
        .size(8.dp)
        .background(
            color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
            shape = RoundedCornerShape(100)
        )
)

@Composable
private fun TimerZone(
    timerMode: TimerMode,
    isSelectModeMenuShown: Boolean,
    onChangeTimerModeClick: () -> Unit,
    onSendResultClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var isTimerEnabled by rememberSaveable { mutableStateOf(true) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(color = MaterialTheme.colorScheme.primary)
            .padding(vertical = 12.dp, horizontal = 20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextButton(
                onClick = onChangeTimerModeClick,
                enabled = isTimerEnabled
            ) {
                Text(
                    text = timerMode.label,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(end = 2.dp)
                )

                val icon = if (isSelectModeMenuShown) R.drawable.keyboard_arrow_up else R.drawable.keyboard_arrow_down
                Icon(
                    imageVector = ImageVector.vectorResource(icon),
                    contentDescription = "Timer mode",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        when (timerMode) {
            TimerMode.TIMER -> {
                val timer = remember { Timer() }

                ClickableTimer(
                    formattedTime = timer.formattedTime,
                    isActive = timer.state == TimerState.ACTIVE,
                    isEnabled = isTimerEnabled,
                    penalty = timer.penalty,
                    onStartClick = {
                        timer.reset()
                        timer.start()
                    },
                    onStopClick = {
                        timer.stop()
                        isTimerEnabled = false
                    },
                    onSendResultClick = {
                        onSendResultClick(timer.timeInMills)
                        timer.reset()
                        isTimerEnabled = true
                    },
                    onPlusTwoClick = { timer.managePlusTwoPenalty() },
                    onDnfClick = { timer.manageDnfPenalty() },
                    modifier = Modifier.fillMaxSize()
                )
            }
            TimerMode.TYPING -> {
                ManualTypingTimer(
                    onSendResultClick = { onSendResultClick(12L) },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimerModeModalBottomSheet(
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    onItemSelect: (TimerMode) -> Unit,
    modifier: Modifier = Modifier
) = ModalBottomSheet(
    sheetState = sheetState,
    onDismissRequest = onDismissRequest,
    containerColor = MaterialTheme.colorScheme.background,
    dragHandle = { BottomSheetDefaults.DragHandle(color = MaterialTheme.colorScheme.primary) },
    modifier = modifier
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
        TextButton(onClick = { onItemSelect(TimerMode.TIMER) }) {
            Text(
                text = TimerMode.TIMER.label,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth()
            )
        }

        TextButton(onClick = { onItemSelect(TimerMode.TYPING) }) {
            Text(
                text = TimerMode.TYPING.label,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun PreviewRoomScreen() {
//    RoomScreen(
//        room = Room(
//            name = "Room name",
//            event = Event.FIVE_BY_FIVE,
//            isOpen = true,
//            password = null
//        ),
//        onNavigationButtonClick = { },
//        onActionButtonClick = { },
//        modifier = Modifier.fillMaxSize()
//    )
}