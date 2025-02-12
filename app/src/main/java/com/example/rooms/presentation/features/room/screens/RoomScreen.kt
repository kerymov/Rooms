package com.example.rooms.presentation.features.room.screens

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
import androidx.compose.foundation.layout.statusBarsPadding
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
import com.example.rooms.R
import com.example.rooms.presentation.features.main.rooms.models.EventUi
import com.example.rooms.presentation.features.main.rooms.models.PenaltyUi
import com.example.rooms.presentation.features.main.rooms.models.ResultUi
import com.example.rooms.presentation.features.main.rooms.models.RoomDetailsUi
import com.example.rooms.presentation.features.main.rooms.models.ScrambleUi
import com.example.rooms.presentation.features.main.rooms.models.SettingsUi
import com.example.rooms.presentation.features.main.rooms.models.SolveUi
import com.example.rooms.presentation.features.room.components.ClickableTimer
import com.example.rooms.presentation.features.room.components.ManualTypingTimer
import com.example.rooms.presentation.features.room.components.ScrambleImageCanvas
import com.example.rooms.presentation.features.room.utils.RESULT_PLACEHOLDER
import com.example.rooms.presentation.features.room.utils.Timer
import com.example.rooms.presentation.features.room.utils.TimerState
import com.example.rooms.presentation.features.room.utils.formatRawStringTimeToMills
import com.example.rooms.presentation.features.room.utils.resultInWcaNotation
import com.example.rooms.presentation.features.room.viewModels.RoomUiState
import com.example.rooms.presentation.features.room.viewModels.RoomViewModel
import com.example.rooms.presentation.features.utils.FadeSide
import com.example.rooms.presentation.features.utils.fadingEdge
import com.example.rooms.presentation.theme.RoomsTheme

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
    modifier: Modifier = Modifier,
    roomViewModel: RoomViewModel = viewModel(),
) {
    val roomUiState by roomViewModel.uiState.collectAsState()

    var isResultsSheetOpen by rememberSaveable { mutableStateOf(false) }
    val resultsSheetState = rememberModalBottomSheetState()

    Content(
        state = roomUiState,
        onShowResults = { isResultsSheetOpen = true },
        onSolveResultSend = roomViewModel::sendSolveResult,
        modifier = modifier
    )

    if (isResultsSheetOpen) {
        RoomResultsBottomSheet(
            sheetState = resultsSheetState,
            onDismissRequest = { isResultsSheetOpen = false },
            users = roomUiState.users,
            solves = roomUiState.solves.reversed(),
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxSize()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    state: RoomUiState,
    onShowResults: () -> Unit,
    onSolveResultSend: (time: Long, penalty: PenaltyUi) -> Unit,
    modifier: Modifier = Modifier
) {
    var timerMode by rememberSaveable { mutableStateOf(TimerMode.TIMER) }
    var isTimerModeSheetOpen by rememberSaveable { mutableStateOf(false) }
    val timerModeSheetState = rememberModalBottomSheetState()

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
            onShowResults = onShowResults,
            modifier = Modifier.fillMaxWidth()
        )
        TimerZone(
            timerMode = timerMode,
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
    isSelectModeMenuShown: Boolean,
    onChangeTimerModeClick: () -> Unit,
    onSendResultClick: (Long, PenaltyUi) -> Unit,
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
                enabled = isTimerEnabled,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
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
                val timer = remember { Timer() }

                ClickableTimer(
                    formattedTime = timer.formattedTime,
                    penalty = timer.penalty,
                    isEnabled = isTimerEnabled,
                    isActive = timer.state == TimerState.ACTIVE,
                    onStart = {
                        timer.reset()
                        timer.start()
                    },
                    onStop = {
                        timer.stop()
                        isTimerEnabled = false
                    },
                    onDismiss = timer::reset,
                    onSendResultClick = {
                        onSendResultClick(
                            timer.timeInMills,
                            PenaltyUi.entries.find {
                                it.ordinal == timer.penalty.ordinal
                            } ?: PenaltyUi.NO_PENALTY
                        )
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
                    onSendResultClick = { timeString ->
                        onSendResultClick(
                            timeString.formatRawStringTimeToMills(),
                            PenaltyUi.NO_PENALTY
                        )
                    },
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
                    results = listOf(ResultUi("admin", 9830, PenaltyUi.PLUS_TWO))
                ),
                isWaitingForNewScramble = false,
                users = listOf("admin"),
                solves = emptyList()
            ),
            onShowResults = { },
            onSolveResultSend = { _, _ -> },
            modifier = Modifier.fillMaxSize()
        )
    }
}