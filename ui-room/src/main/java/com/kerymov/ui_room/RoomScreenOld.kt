package com.kerymov.ui_room

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.NavigateNext
import androidx.compose.material3.BottomSheetDefaults
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
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kerymov.ui_common_speedcubing.models.EventUi
import com.kerymov.ui_common_speedcubing.models.PenaltyUi
import com.kerymov.ui_common_speedcubing.models.ResultUi
import com.kerymov.ui_common_speedcubing.models.ScrambleUi
import com.kerymov.ui_common_speedcubing.models.SolveUi
import com.kerymov.ui_core.components.CustomAlertDialog
import com.kerymov.ui_core.components.CustomAlertDialogDefaults
import com.kerymov.ui_core.theme.RoomsTheme
import com.kerymov.ui_core.utils.FadeSide
import com.kerymov.ui_core.utils.defaultBottomSheetPadding
import com.kerymov.ui_core.utils.fadingEdge
import com.kerymov.ui_room.components.ManualTypingTimer
import com.kerymov.ui_room.components.ScrambleImageCanvas
import com.kerymov.ui_room.models.RoomDetailsUi
import com.kerymov.ui_room.models.SettingsUi
import com.kerymov.ui_room.screens.RoomResultsBottomSheet
import com.kerymov.ui_room.utils.RESULT_PLACEHOLDER
import com.kerymov.ui_room.utils.TimerRunner
import com.kerymov.ui_room.utils.formatRawStringTimeToMills
import com.kerymov.ui_room.utils.resultInWcaNotation
import com.kerymov.ui_room.viewModels.RoomUiState
import com.kerymov.ui_room.viewModels.RoomViewModel
import com.kerymov.ui_room.viewModels.TimerMode

private enum class Page {
    SCRAMBLE,
    SCRAMBLE_IMAGE
}

@Composable
fun RoomScreenOld(
    modifier: Modifier = Modifier,
    onExit: () -> Unit,
    roomViewModel: RoomViewModel = viewModel(),
) {
    val roomUiState by roomViewModel.uiState.collectAsState()

    BackHandler {
        roomViewModel.toggleExitConfirmationDialog(true)
    }

    if (roomUiState.isExitConfirmationDialogShown) {
        CustomAlertDialog(
            title = "Leave the room",
            message = "Do you really want to leave the room?",
            onDismiss = { roomViewModel.toggleExitConfirmationDialog(false) },
            onConfirm = {
                roomViewModel.toggleExitConfirmationDialog(false)
                onExit()
            },
            dismissButtonText = "Cancel",
            confirmButtonText = "Leave",
            colors = CustomAlertDialogDefaults.alertColors(
                confirmButtonColor = MaterialTheme.colorScheme.error
            )
        )
    }

    Content(
        state = roomUiState,
        onSolveResultSend = { _, _ ->
            roomViewModel.sendSolveResult()
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    state: RoomUiState,
    onSolveResultSend: (time: Long, penalty: PenaltyUi) -> Unit,
    modifier: Modifier = Modifier
) {
    var timerMode by rememberSaveable { mutableStateOf(TimerMode.TIMER) }
    var isTimerModeSheetOpen by rememberSaveable { mutableStateOf(false) }
    val timerModeSheetState = rememberModalBottomSheetState()

    var isResultsSheetOpen by rememberSaveable { mutableStateOf(false) }
    val resultsSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        ScrambleZone(
            scramble = state.currentSolve?.scramble,
            event = state.roomDetails.settings.event,
            isWaitingForNewScramble = state.isWaitingForNewScramble,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        CurrentSolveResults(
            users = state.users,
            results = state.currentSolve?.results ?: emptyList(),
            onShowResults = { isResultsSheetOpen = true },
            modifier = Modifier.fillMaxWidth()
        )
        TimerZone(
            timerMode = timerMode,
            isTimerEnabled = !state.isWaitingForNewScramble,
            isSelectModeMenuShown = isTimerModeSheetOpen,
            onChangeTimerModeClick = {
                isTimerModeSheetOpen = !isTimerModeSheetOpen
            },
            onSendResultClick = { timeInMills, penalty ->
                onSolveResultSend(timeInMills, penalty)
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
        )
    }

    if (isTimerModeSheetOpen) {
        TimerModeModalBottomSheet(
            sheetState = timerModeSheetState,
            onDismissRequest = { isTimerModeSheetOpen = false },
            onItemSelect = { mode ->
                timerMode = mode
                isTimerModeSheetOpen = false
            },
            modifier = Modifier.fillMaxWidth()
        )
    }

    if (isResultsSheetOpen) {
        RoomResultsBottomSheet(
            sheetState = resultsSheetState,
            onDismissRequest = { isResultsSheetOpen = false },
            users = state.users,
            solves = state.solves,
            modifier = Modifier
                .fillMaxSize()
                .defaultBottomSheetPadding()
        )
    }
}

@Composable
private fun CurrentSolveResults(
    users: List<String>,
    results: List<ResultUi>,
    onShowResults: () -> Unit,
    modifier: Modifier = Modifier
) = Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween,
    modifier = modifier
) {
    val listState = rememberLazyListState()

    LazyRow(
        state = listState,
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .weight(1f)
            .fadingEdge(
                FadeSide.RIGHT,
                color = MaterialTheme.colorScheme.background,
                width = 32.dp,
                spec = tween(500),
                isVisible = listState.canScrollForward
            )
    ) {
        items(users) { username ->
            val result = results.find { it.userName == username }
            val (time, penalty) = result?.time to result?.penalty

            val formattedResult = time?.let {
                resultInWcaNotation(it, penalty ?: PenaltyUi.NO_PENALTY)
            } ?: RESULT_PLACEHOLDER

            ResultPill(
                username = username,
                result = formattedResult
            )
        }
    }

    TextButton(
        onClick = onShowResults,
        contentPadding = PaddingValues(
            top = ButtonDefaults.TextButtonContentPadding.calculateTopPadding(),
            bottom = ButtonDefaults.TextButtonContentPadding.calculateTopPadding(),
            start = 4.dp,
            end = 0.dp
        ),
        modifier = Modifier.padding(end = 4.dp)
    ) {
        Text(
            text = "All",
            style = MaterialTheme.typography.titleMedium.copy(
                platformStyle = PlatformTextStyle(includeFontPadding = true)
            ),
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.NavigateNext,
            contentDescription = "Show all results",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ResultPill(
    username: String,
    result: String
) = Chip(
    onClick = {},
    shape = RoundedCornerShape(100),
    border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.primary),
    colors = ChipDefaults.chipColors(
        backgroundColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimary
    )
) {
    Text(
        text = "$username: $result",
        style = MaterialTheme.typography.labelLarge,
        textAlign = TextAlign.Center,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.padding(4.dp)
    )
}

@Composable
private fun ScrambleZone(
    scramble: ScrambleUi?,
    event: EventUi,
    isWaitingForNewScramble: Boolean,
    modifier: Modifier = Modifier
) {
    if (scramble == null || isWaitingForNewScramble) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
        ) {
            Text(
                text = "Waiting for a scramble...",
                style = MaterialTheme.typography.titleMedium,
                color = contentColorFor(backgroundColor = MaterialTheme.colorScheme.background),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    } else {
        val pagerState = rememberPagerState(
            initialPage = Page.SCRAMBLE.ordinal,
            initialPageOffsetFraction = 0f
        ) { 2 }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(vertical = 12.dp, horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                key = { Page.entries[it].ordinal },
                pageSize = PageSize.Fixed(300.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { page ->
                when (page) {
                    Page.SCRAMBLE.ordinal -> {
                        Text(
                            text = scramble.scramble,
                            style = MaterialTheme.typography.bodyMedium,
                            color = contentColorFor(backgroundColor = MaterialTheme.colorScheme.background),
                            textAlign = TextAlign.Center,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 16.dp)
                        )
                    }

                    Page.SCRAMBLE_IMAGE.ordinal -> {
                        ScrambleImageCanvas(
                            image = scramble.image ?: ScrambleUi.Image(listOf()),
                            event = event,
                            modifier = Modifier.fillMaxSize()
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
    isTimerEnabled: Boolean,
    isSelectModeMenuShown: Boolean,
    onChangeTimerModeClick: () -> Unit,
    onSendResultClick: (Long, PenaltyUi) -> Unit,
    modifier: Modifier = Modifier
) {
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
                enabled = isTimerEnabled,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
                )
            ) {
                Text(
                    text = timerMode.label,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(end = 2.dp)
                )

                val icon =
                    if (isSelectModeMenuShown) R.drawable.keyboard_arrow_up else R.drawable.keyboard_arrow_down
                Icon(
                    imageVector = ImageVector.vectorResource(icon),
                    contentDescription = "Timer mode",
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        when (timerMode) {
            TimerMode.TIMER -> {
                val timerRunner = remember { TimerRunner() }

//                ClickableTimer(
//                    formattedTime = timerRunner.formattedTime,
//                    penalty = timerRunner.penalty,
//                    isEnabled = isTimerEnabled,
//                    isActive = timerRunner.state == TimerRunnerState.ACTIVE,
//                    onStart = {
//                        timerRunner.reset()
//                        timerRunner.start()
//                    },
//                    onStop = {
//                        timerRunner.stop()
//                    },
//                    onDismiss = timerRunner::reset,
//                    onSendResultClick = {
//                        onSendResultClick(
//                            timerRunner.timeInMills,
//                            PenaltyUi.entries.find {
//                                it.ordinal == timerRunner.penalty.ordinal
//                            } ?: PenaltyUi.NO_PENALTY
//                        )
//                        timerRunner.reset()
//                    },
//                    onPlusTwoClick = { timerRunner.setPlusTwoPenalty() },
//                    onDnfClick = { timerRunner.setDnfPenalty() },
//                    modifier = Modifier.fillMaxSize()
//                )
            }

            TimerMode.TYPING -> {
                ManualTypingTimer(
                    onSendResultClick = { timeString ->
                        onSendResultClick(
                            timeString.formatRawStringTimeToMills(),
                            PenaltyUi.NO_PENALTY
                        )
                    },
                    isEnabled = isTimerEnabled,
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
    windowInsets: WindowInsets = WindowInsets(0, 0, 0, 0),
    modifier: Modifier = Modifier
) = ModalBottomSheet(
    sheetState = sheetState,
    onDismissRequest = onDismissRequest,
    containerColor = MaterialTheme.colorScheme.background,
    dragHandle = { BottomSheetDefaults.DragHandle(color = MaterialTheme.colorScheme.primary) },
    windowInsets = windowInsets,
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
private fun PreviewRoomScreenContent() {
    val scramble = "F2 D2 L2 D U2 R2 U' B2 R2 F' D' F R' U B' U F' U2"
    val image = ScrambleUi.Image(
        faces = listOf(
            ScrambleUi.Face(
                colors = listOf(
                    listOf(2, 4, 5),
                    listOf(3, 0, 4),
                    listOf(0, 4, 4)
                )
            ),
            ScrambleUi.Face(
                colors = listOf(
                    listOf(0, 5, 1),
                    listOf(0, 1, 0),
                    listOf(3, 2, 0)
                )
            ),
            ScrambleUi.Face(
                colors = listOf(
                    listOf(4, 1, 1),
                    listOf(2, 2, 5),
                    listOf(2, 4, 5)
                )
            ),
            ScrambleUi.Face(
                colors = listOf(
                    listOf(5, 2, 5),
                    listOf(3, 3, 3),
                    listOf(4, 0, 1)
                )
            ),
            ScrambleUi.Face(
                colors = listOf(
                    listOf(3, 3, 1),
                    listOf(1, 4, 5),
                    listOf(2, 1, 2)
                )
            ),
            ScrambleUi.Face(
                colors = listOf(
                    listOf(3, 0, 4),
                    listOf(2, 5, 1),
                    listOf(3, 5, 0)
                )
            )
        )
    )

    RoomsTheme {
        Content(
            state = RoomUiState(
                roomDetails = RoomDetailsUi(
                    id = "Test",
                    name = "Test",
                    password = "",
                    administratorName = "admin",
                    cachedScrambles = emptyList(),
                    connectedUserNames = listOf("admin"),
                    wasOnceConnectedUserNames = listOf("admin"),
                    solves = emptyList(),
                    settings = SettingsUi(event = EventUi.THREE_BY_THREE)
                ),
                currentSolve = SolveUi(
                    solveNumber = 2,
                    scramble = ScrambleUi(scramble = scramble, image = image),
                    results = listOf(
                        ResultUi(
                            "admin",
                            9830,
                            PenaltyUi.PLUS_TWO
                        )
                    )
                ),
                isWaitingForNewScramble = false,
                users = listOf("admin"),
                solves = emptyList(),
                timerMode = TimerMode.TIMER
            ),
            onSolveResultSend = { _, _ -> },
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
        )
    }
}