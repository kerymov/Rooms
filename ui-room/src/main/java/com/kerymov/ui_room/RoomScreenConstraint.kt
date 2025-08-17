package com.kerymov.ui_room

import ModeSwitch
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Keyboard
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.Visibility
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kerymov.ui_common_speedcubing.models.EventUi
import com.kerymov.ui_common_speedcubing.models.PenaltyUi
import com.kerymov.ui_common_speedcubing.models.ResultUi
import com.kerymov.ui_common_speedcubing.models.ScrambleUi
import com.kerymov.ui_common_speedcubing.models.SolveUi
import com.kerymov.ui_core.components.CustomAlertDialog
import com.kerymov.ui_core.components.CustomAlertDialogDefaults
import com.kerymov.ui_core.components.Divider
import com.kerymov.ui_core.theme.RoomsTheme
import com.kerymov.ui_core.theme.scramble
import com.kerymov.ui_core.utils.FadeSide
import com.kerymov.ui_core.utils.IconSource
import com.kerymov.ui_core.utils.LocalUser
import com.kerymov.ui_core.utils.defaultBottomSheetPadding
import com.kerymov.ui_core.utils.fadingEdge
import com.kerymov.ui_room.components.InfoCard
import com.kerymov.ui_room.components.ScrambleImageCanvas
import com.kerymov.ui_room.components.SolveManagementButtons
import com.kerymov.ui_room.components.StatisticItem
import com.kerymov.ui_room.models.RoomDetailsUi
import com.kerymov.ui_room.models.SettingsUi
import com.kerymov.ui_room.screens.RoomResultsBottomSheet
import com.kerymov.ui_room.utils.MAX_TIME_LENGTH
import com.kerymov.ui_room.utils.RESULT_PLACEHOLDER
import com.kerymov.ui_room.utils.ResultInfo
import com.kerymov.ui_room.utils.StatisticManager
import com.kerymov.ui_room.utils.TimerRunnerState
import com.kerymov.ui_room.utils.TimerVisualTransformation
import com.kerymov.ui_room.utils.resultInWcaNotation
import com.kerymov.ui_room.utils.toWcaNotation
import com.kerymov.ui_room.viewModels.RoomUiState
import com.kerymov.ui_room.viewModels.RoomViewModel
import com.kerymov.ui_room.viewModels.TimerMode
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomScreen(
    modifier: Modifier = Modifier,
    onExit: () -> Unit,
    onTopAppBarVisibilityChange: (Boolean) -> Unit,
    roomViewModel: RoomViewModel = viewModel(),
) {
    val roomUiState by roomViewModel.uiState.collectAsState()

    val resultsSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

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

    if (roomUiState.isResultsSheetOpen) {
        RoomResultsBottomSheet(
            sheetState = resultsSheetState,
            onDismissRequest = {
                roomViewModel.toggleResultsSheet(false)
            },
            users = roomUiState.users,
            solves = roomUiState.solves,
            modifier = Modifier
                .fillMaxSize()
                .defaultBottomSheetPadding()
        )
    }

    Content(
        state = roomUiState,
        onTimerStart = roomViewModel::startTimer,
        onTimerStop = roomViewModel::stopTimer,
        onTimerReset = roomViewModel::resetTimer,
        onTypingTimeTextChange = roomViewModel::updateTypingInputTimeText,
        onToggleTypingEditingMode = roomViewModel::toggleTypingEditingMode,
        onUpdatePenalty = roomViewModel::updatePenalty,
        onSendResultClick = roomViewModel::sendSolveResult,
        onShowResultsClick = roomViewModel::toggleResultsSheet,
        onToggleTimerMode = roomViewModel::toggleTimerMode,
        onToggleActionButtonsVisibility = roomViewModel::toggleActionButtonsVisibility,
        onToggleTopAppBarVisibility = onTopAppBarVisibilityChange,
        modifier = modifier.fillMaxSize()
    )
}

@Composable
private fun Content(
    state: RoomUiState,
    onTimerStart: () -> Unit,
    onTimerStop: () -> Unit,
    onTimerReset: () -> Unit,
    onTypingTimeTextChange: (value: String) -> Unit,
    onToggleTypingEditingMode: (isEditing: Boolean) -> Unit,
    onUpdatePenalty: (penalty: PenaltyUi) -> Unit,
    onShowResultsClick: (isShown: Boolean) -> Unit,
    onSendResultClick: () -> Unit,
    onToggleTimerMode: (mode: TimerMode) -> Unit,
    onToggleActionButtonsVisibility: (isVisible: Boolean) -> Unit,
    onToggleTopAppBarVisibility: (isVisible: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(state.timerState.runnerState) {
        onToggleTopAppBarVisibility(state.timerState.runnerState != TimerRunnerState.ACTIVE)
    }

    val defaultTimerColor = MaterialTheme.colorScheme.onBackground
    val preparationColor = Color.Red
    val readyTimerColor = Color.Green
    val disabledTimerColor = MaterialTheme.colorScheme.onBackground.copy(0.5f)
    var timerColor by remember { mutableStateOf(defaultTimerColor) }

    val isTimerEnabled = !state.isWaitingForNewScramble &&
            state.timerState.runnerState != TimerRunnerState.STOPPED
    val isTimerActive = state.timerState.runnerState == TimerRunnerState.ACTIVE

    var isTimerPressed by rememberSaveable { mutableStateOf(false) }
    val timerPreparationCoroutineScope = rememberCoroutineScope()

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current

    val resultsListState = rememberLazyListState()

    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .pointerInput(
                isTimerEnabled,
                isTimerActive,
                state.timerMode,
                state.typingState,
            ) {
                if (!isTimerEnabled) return@pointerInput

                detectTapGestures(
                    onPress = { _ ->
                        if (isTimerActive || state.timerMode == TimerMode.TYPING) return@detectTapGestures

                        isTimerPressed = true
                        timerColor = preparationColor
                        val pressStartTime = System.currentTimeMillis()

                        val preparationJob = timerPreparationCoroutineScope.launch {
                            delay(500)
                            if (isTimerPressed) {
                                timerColor = readyTimerColor
                            }
                        }

                        try {
                            awaitRelease()

                            timerColor = defaultTimerColor
                            preparationJob.cancel()

                            val pressingDuration = System.currentTimeMillis() - pressStartTime
                            if (pressingDuration > 500) {
                                onTimerReset()
                                onTimerStart()
                            }
                        } catch (e: Exception) {
                            onTimerReset()
                            preparationJob.cancel()
                        }
                    },
                    onTap = {
                        when (state.timerMode) {
                            TimerMode.TIMER -> {
                                if (!isTimerActive) return@detectTapGestures

                                onTimerStop()
                                onToggleActionButtonsVisibility(true)
                            }

                            TimerMode.TYPING -> {
                                onToggleTypingEditingMode(!state.typingState.isInEditingMode)
                            }
                        }
                    },
                )
            }
            .padding(start = 12.dp, end = 12.dp, top = 4.dp, bottom = 8.dp)
    ) {
        ConstraintLayout(
            modifier = Modifier.matchParentSize()
        ) {
            val (
                topToolbar,
                scrambleView,
                currentSolveView,
                solveManagementButtons,
                bottomToolbar,
            ) = createRefs()

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .constrainAs(topToolbar) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                        visibility = if (isTimerActive) Visibility.Gone else Visibility.Visible
                    }
            ) {
                ModeSwitch(
                    checked = state.timerMode == TimerMode.TYPING,
                    onCheckedChange = {
                        val mode = if (state.timerMode == TimerMode.TYPING) {
                            TimerMode.TIMER
                        } else {
                            TimerMode.TYPING
                        }
                        onToggleTimerMode(mode)
                    },
                    leftIcon = IconSource.Vector(Icons.Rounded.Timer),
                    rightIcon = IconSource.Vector(Icons.Rounded.Keyboard),
                    leftContentDescription = "Manual Mode",
                    rightContentDescription = "Timer Mode",
                    enabled = !state.isActionButtonsVisible,
                    modifier = Modifier
                        .height(48.dp)
                )

                CurrentSolveResults(
                    users = state.users,
                    results = state.currentSolve?.results ?: emptyList(),
                    listState = resultsListState,
                    onShowResultsClick = { onShowResultsClick(true) },
                    modifier = Modifier
                        .height(48.dp)
                        .weight(1f)
                )
            }

            ScrambleView(
                scramble = state.currentSolve?.scramble,
                isWaitingForNewScramble = state.isWaitingForNewScramble,
                modifier = Modifier
                    .constrainAs(scrambleView) {
                        top.linkTo(topToolbar.bottom, margin = 8.dp)
                        bottom.linkTo(currentSolveView.top, margin = 8.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        visibility = if (isTimerActive) Visibility.Gone else Visibility.Visible
                    }
            )

            Box(
                modifier = Modifier
                    .constrainAs(currentSolveView) {
                        centerTo(parent)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                when (state.timerMode) {
                    TimerMode.TIMER -> TimerSolveView(
                        formattedTime = state.timerState.formattedTime,
                        penalty = state.timerState.penalty,
                        textColor = if (!state.isWaitingForNewScramble) timerColor else disabledTimerColor
                    )

                    TimerMode.TYPING -> {
                        LaunchedEffect(state.typingState.isInEditingMode) {
                            keyboard?.let {
                                if (state.typingState.isInEditingMode) {
                                    focusRequester.requestFocus()
                                    focusRequester.captureFocus()
                                    it.show()
                                } else {
                                    focusManager.clearFocus()
                                    it.hide()
                                }
                            }
                        }

                        TypingSolveView(
                            formattedTime = state.typingState.formattedTime,
                            rawInputTime = state.typingState.inputTimeText,
                            onTimeChange = {
                                val newText = it.filter { symbol -> symbol.isDigit() }
                                if (newText.length <= MAX_TIME_LENGTH) onTypingTimeTextChange(it)
                            },
                            isEditing = state.typingState.isInEditingMode,
                            onDone = {
                                onToggleTypingEditingMode(false)
                            },
                            penalty = state.typingState.penalty,
                            textColor = if (state.isWaitingForNewScramble || state.typingState.inputTimeText.isBlank()) {
                                disabledTimerColor
                            } else {
                                timerColor
                            },
                            focusManager = focusManager,
                            modifier = Modifier
                                .focusRequester(focusRequester)
                                .onFocusChanged {
                                    Log.d("Focuss", "Focus changed: ${it.isFocused}, Input text: ${state.typingState.inputTimeText}")
                                    onToggleActionButtonsVisibility(
                                        !it.isFocused && state.typingState.inputTimeText.isNotBlank()
                                    )
                                }
                        )
                    }
                }
            }

            SolveManagementButtons(
                penalty = when(state.timerMode) {
                    TimerMode.TIMER -> state.timerState.penalty
                    TimerMode.TYPING -> state.typingState.penalty
                },
                onPenaltyChange = { penalty ->
                    onUpdatePenalty(penalty)
                },
                onSendResultClick = {
                    onSendResultClick()
                    onTimerReset()

                    onToggleActionButtonsVisibility(false)
                },
                modifier = Modifier
                    .constrainAs(solveManagementButtons) {
                        top.linkTo(currentSolveView.bottom, margin = 8.dp)
                        bottom.linkTo(bottomToolbar.top, margin = 8.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.wrapContent
                        verticalBias = 0f
                        visibility = if (state.isActionButtonsVisible) Visibility.Visible else Visibility.Gone
                    }
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .constrainAs(bottomToolbar) {
                        top.linkTo(currentSolveView.bottom)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                        verticalBias = 1f
                        visibility = if (isTimerActive) Visibility.Gone else Visibility.Visible
                    }
            ) {
                InfoCard(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                ) {
                    ScrambleImageCanvas(
                        image = state.currentSolve?.scramble?.image ?: ScrambleUi.Image(listOf()),
                        event = state.roomDetails.settings.event,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                InfoCard(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                ) {
                    val localUser = LocalUser.current

                    UserStatistics(
                        results = state.solves.flatMap { solve ->
                            solve.results.filter { it.userName == localUser?.username }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun TimerSolveView(
    formattedTime: String,
    modifier: Modifier = Modifier,
    penalty: PenaltyUi = PenaltyUi.NO_PENALTY,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
) = Text(
    text = when (penalty) {
        PenaltyUi.DNF -> "DNF"
        PenaltyUi.PLUS_TWO -> "$formattedTime+"
        PenaltyUi.NO_PENALTY -> formattedTime
    },
    style = MaterialTheme.typography.displayMedium,
    color = textColor,
    textAlign = TextAlign.Center,
    modifier = modifier
)

@Composable
private fun TypingSolveView(
    formattedTime: String,
    rawInputTime: String,
    onTimeChange: (String) -> Unit,
    isEditing: Boolean,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
    penalty: PenaltyUi = PenaltyUi.NO_PENALTY,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    focusManager: FocusManager = LocalFocusManager.current
) = DisableSelection {
    if (isEditing) {
        BasicTextField(
            value = TextFieldValue(text = rawInputTime, selection = TextRange(rawInputTime.length)),
            onValueChange = { onTimeChange(it.text) },
            visualTransformation = TimerVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    if (formattedTime.isNotBlank()) {
                        onDone()
                    }
                }
            ),
            readOnly = false,
            enabled = true,
            singleLine = true,
            textStyle = MaterialTheme.typography.displayMedium.copy(
                textAlign = TextAlign.Center,
                color = textColor,
            ),
            modifier = modifier.wrapContentSize()
        )
    } else {
        Text(
            text = when (penalty) {
                PenaltyUi.DNF -> "DNF"
                PenaltyUi.PLUS_TWO -> "$formattedTime+"
                PenaltyUi.NO_PENALTY -> formattedTime
            },
            style = MaterialTheme.typography.displayMedium.copy(
                textAlign = TextAlign.Center,
                color = textColor
            ),
            modifier = modifier.wrapContentSize()
        )
    }
}

@Composable
private fun CurrentSolveResults(
    users: List<String>,
    results: List<ResultUi>,
    listState: LazyListState,
    onShowResultsClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(12.dp)
) = LazyRow(
    state = listState,
    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
    horizontalArrangement = Arrangement.spacedBy(4.dp),
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier
        .clip(shape)
        .background(color = MaterialTheme.colorScheme.onPrimary)
        .fadingEdge(
            FadeSide.LEFT,
            color = MaterialTheme.colorScheme.onPrimary,
            width = 16.dp,
            spec = tween(200),
            isVisible = listState.canScrollBackward
        )
        .fadingEdge(
            FadeSide.RIGHT,
            color = MaterialTheme.colorScheme.onPrimary,
            width = 16.dp,
            spec = tween(200),
            isVisible = listState.canScrollForward
        )
        .clickable {
            onShowResultsClick()
        }
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

@Composable
private fun ResultPill(
    username: String,
    result: String
) = Surface(
    shape = RoundedCornerShape(100),
    border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.primary),
    color = MaterialTheme.colorScheme.primaryContainer,
    contentColor = MaterialTheme.colorScheme.onPrimary,
) {
    Text(
        text = "$username: $result",
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Center,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.padding(8.dp)
    )
}

@Composable
private fun ScrambleView(
    scramble: ScrambleUi?,
    isWaitingForNewScramble: Boolean,
    modifier: Modifier = Modifier,
    contentColor: Color = contentColorFor(backgroundColor = MaterialTheme.colorScheme.background)
) = Box(
    contentAlignment = Alignment.Center,
    modifier = modifier
) {
    if (scramble == null || isWaitingForNewScramble) {
        Text(
            text = "Waiting for a scramble...",
            style = MaterialTheme.typography.scramble,
            color = contentColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    } else {
        val scrambleText = scramble.scramble.replace(" ", " ".repeat(2))
        Text(
            text = scrambleText,
            style = MaterialTheme.typography.scramble,
            color = contentColor,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            modifier = modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun UserStatistics(
    results: List<ResultUi>,
    modifier: Modifier = Modifier
) = Column(
    modifier = modifier.padding(8.dp)
) {
    val resultsInfo = results.map {
        ResultInfo(time = it.time, isDnf = it.penalty == PenaltyUi.DNF)
    }

    val statistic = StatisticManager.getStatistic(resultsInfo)
    val defaultRowModifier = Modifier.weight(1f)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        StatisticItem(
            label = "best",
            value = statistic.bestTime.toWcaNotation(),
            modifier = defaultRowModifier
        )
        Spacer(Modifier.width(2.dp))
        StatisticItem(
            label = "worst",
            value = statistic.worstTime.toWcaNotation(),
            modifier = defaultRowModifier
        )
    }
    Divider(
        orientation = Orientation.Horizontal,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
    )
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        StatisticItem(
            label = "ao5",
            value = statistic.averageOfFive.toWcaNotation(),
            modifier = defaultRowModifier
        )
        Spacer(Modifier.width(2.dp))
        StatisticItem(
            label = "ao12",
            value = statistic.averageOfTwelve.toWcaNotation(),
            modifier = defaultRowModifier
        )
    }
    Divider(
        orientation = Orientation.Horizontal,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
    )
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        StatisticItem(
            label = "ao100",
            value = statistic.averageOfHundred.toWcaNotation(),
            modifier = defaultRowModifier
        )
        Spacer(Modifier.width(2.dp))
        StatisticItem(
            label = "mean",
            value = statistic.mean.toWcaNotation(),
            modifier = defaultRowModifier
        )
    }
}

@Preview
@Composable
private fun PreviewRoomScreenContent() {
    val scramble =
        "F2 D2 L2 D U2 R2 U' B2 R2 F' D' F R' U B' U F' U2 R2 U' B2 R2 F' D' F R' U B' U F' " +
                "U2 R2 U' B2 R2 F' D' F R' U B' U F' U2 R2 U' B2 R2 F' D' F R' U B' U F' U2"
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
                        ),
                        ResultUi(
                            "user",
                            10023,
                            PenaltyUi.NO_PENALTY
                        ),
                        ResultUi(
                            "username",
                            9830,
                            PenaltyUi.DNF
                        ),
                        ResultUi(
                            "long username",
                            2155,
                            PenaltyUi.NO_PENALTY
                        ),
                        ResultUi(
                            "guest",
                            1030,
                            PenaltyUi.DNF
                        )
                    )
                ),
                isWaitingForNewScramble = false,
                users = listOf("admin", "user", "username", "long username", "guest"),
                solves = emptyList()
            ),
            onTimerStart = { },
            onTimerStop = { },
            onTimerReset = { },
            onTypingTimeTextChange = { },
            onToggleTypingEditingMode = { },
            onUpdatePenalty = { },
            onShowResultsClick = { _ -> },
            onSendResultClick = { },
            onToggleTimerMode = { },
            onToggleActionButtonsVisibility = { },
            onToggleTopAppBarVisibility = { },
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview
@Composable
private fun CurrentSolveResultsPreview() {
    RoomsTheme {
        CurrentSolveResults(
            users = listOf("admin", "user", "username", "long username", "guest"),
            results = listOf(
                ResultUi(
                    "admin",
                    9830,
                    PenaltyUi.PLUS_TWO
                ),
                ResultUi(
                    "user",
                    10023,
                    PenaltyUi.NO_PENALTY
                ),
                ResultUi(
                    "username",
                    9830,
                    PenaltyUi.DNF
                ),
                ResultUi(
                    "long username",
                    2155,
                    PenaltyUi.NO_PENALTY
                ),
                ResultUi(
                    "guest",
                    1030,
                    PenaltyUi.DNF
                )
            ),
            listState = rememberLazyListState(),
            onShowResultsClick = { },
            modifier = Modifier
        )
    }
}