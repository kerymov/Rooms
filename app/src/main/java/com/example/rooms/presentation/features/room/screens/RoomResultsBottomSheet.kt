package com.example.rooms.presentation.features.room.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rooms.presentation.components.Divider
import com.example.rooms.presentation.features.main.rooms.models.PenaltyUi
import com.example.rooms.presentation.features.main.rooms.models.ResultUi
import com.example.rooms.presentation.features.main.rooms.models.ScrambleUi
import com.example.rooms.presentation.features.main.rooms.models.SolveUi
import com.example.rooms.presentation.features.room.utils.RESULT_PLACEHOLDER
import com.example.rooms.presentation.features.room.utils.ResultInfo
import com.example.rooms.presentation.features.room.utils.StatisticManager
import com.example.rooms.presentation.features.room.utils.resultInWcaNotation
import com.example.rooms.presentation.features.room.utils.toWcaNotation
import com.example.rooms.presentation.theme.RoomsTheme
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomResultsBottomSheet(
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    users: List<String>,
    solves: List<SolveUi>,
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = BottomSheetDefaults.windowInsets,
) = ModalBottomSheet(
    sheetState = sheetState,
    onDismissRequest = onDismissRequest,
    dragHandle = { BottomSheetDefaults.DragHandle(color = MaterialTheme.colorScheme.primary) },
    containerColor = MaterialTheme.colorScheme.background,
    windowInsets = windowInsets,
    modifier = modifier.statusBarsPadding()
) {
    Content(
        users = users,
        solves = solves,
        modifier = modifier
            .fillMaxSize()
    )
}

@Composable
private fun Content(
    users: List<String>,
    solves: List<SolveUi>,
    modifier: Modifier = Modifier
) = LazyTable(usernames = users, solves = solves, modifier = modifier)

@Composable
fun LazyTable(
    usernames: List<String>,
    solves: List<SolveUi>,
    modifier: Modifier = Modifier
) = Column(modifier = modifier) {
    val leadingColumnModifier = Modifier
        .padding(horizontal = 4.dp)
        .width(48.dp)
    val defaultColumnModifier = Modifier
        .padding(horizontal = 4.dp)
        .width(96.dp)


    val tableData = solves.map { solve ->
        usernames.map { username ->
            solve.results.find { it.userName == username }
        }
    }

    val horizontalScrollState = rememberScrollState()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary)
            .padding(vertical = 8.dp)
            .horizontalScroll(horizontalScrollState)
    ) {
        TableCellItem(
            text = "#",
            color = MaterialTheme.colorScheme.onPrimary,
            textStyle = MaterialTheme.typography.titleSmall,
            modifier = leadingColumnModifier
        )

        usernames.forEach { username ->
            TableCellItem(
                text = username,
                color = MaterialTheme.colorScheme.onPrimary,
                textStyle = MaterialTheme.typography.titleSmall,
                modifier = defaultColumnModifier
            )
        }
    }

    LazyColumn(
        contentPadding = PaddingValues(vertical = 8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .horizontalScroll(horizontalScrollState)
    ) {
        itemsIndexed(solves) { index, solve ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillParentMaxWidth()
            ) {
                TableCellItem(
                    text = solve.solveNumber.toString(),
                    color = MaterialTheme.colorScheme.outlineVariant,
                    textStyle = MaterialTheme.typography.titleSmall,
                    modifier = leadingColumnModifier
                )

                tableData[index].forEach { rawResult ->
                    val time = rawResult?.time
                    val penalty = rawResult?.penalty

                    val result = time?.let {
                        resultInWcaNotation(it, penalty ?: PenaltyUi.NO_PENALTY)
                    } ?: RESULT_PLACEHOLDER

                    ResultItem(
                        text = result,
                        modifier = defaultColumnModifier
                    )
                }
            }
        }
    }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 12.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(usernames) { username ->
            UserStatistics(username, solves.flatMap { solve ->
                solve.results.filter { it.userName == username }
            })
        }
    }
}

@Composable
private fun TableCellItem(
    text: String,
    color: Color = MaterialTheme.colorScheme.onBackground,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    modifier: Modifier = Modifier
) = Text(
    text = text,
    style = textStyle,
    color = color,
    textAlign = TextAlign.Center,
    maxLines = 1,
    overflow = TextOverflow.Ellipsis,
    modifier = modifier.padding(horizontal = 8.dp, vertical = 4.dp)
)

@Composable
private fun ResultItem(text: String, modifier: Modifier) = Text(
    text = text,
    modifier = modifier.padding(horizontal = 8.dp, vertical = 4.dp),
    textAlign = TextAlign.Center,
    style = MaterialTheme.typography.bodyMedium
)

@Composable
private fun UserStatistics(
    username: String,
    results: List<ResultUi>
) = Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
        .clip(RoundedCornerShape(16.dp))
        .border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.primary,
            shape = RoundedCornerShape(16.dp)
        )
        .width(156.dp)
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        val resultsInfo = results.map {
            ResultInfo(time = it.time, isDnf = it.penalty == PenaltyUi.DNF)
        }

        val statistic = StatisticManager.getStatistic(resultsInfo)

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            StatisticItem(label = "best", value = statistic.bestTime.toWcaNotation())
            StatisticItem(label = "worst", value = statistic.worstTime.toWcaNotation())
        }
        Divider(modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
        )
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            StatisticItem(label = "ao5", value = statistic.averageOfFive.toWcaNotation())
            StatisticItem(label = "ao12", value = statistic.averageOfTwelve.toWcaNotation())
        }
        Divider(modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
        )
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            StatisticItem(label = "ao100", value = statistic.averageOfHundred.toWcaNotation())
            StatisticItem(label = "mean", value = statistic.mean.toWcaNotation())
        }
    }

    Text(
        text = username,
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
private fun StatisticItem(label: String, value: String) = Column(
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Text(
        text = label,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.outlineVariant,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
    Spacer(modifier = Modifier.height(2.dp))
    Text(
        text = value,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onBackground,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Preview(showBackground = true)
@Composable
private fun RoomCreatingSheetContentPreview() {
    val users = listOf("kerymov", "Oleh", "Vlad", "Username", "admin")
    val solves = List(50) { index ->
        SolveUi(
            solveNumber = index + 1,
            scramble = ScrambleUi("", null),
            results = listOf(
                ResultUi("Oleh", Random.nextLong(70000), PenaltyUi.NO_PENALTY),
                ResultUi("kerymov", Random.nextLong(50000), PenaltyUi.NO_PENALTY),
                ResultUi("Vlad", Random.nextLong(10000), PenaltyUi.PLUS_TWO),
                ResultUi("Username", Random.nextLong(10000), PenaltyUi.DNF),
                ResultUi("admin", Random.nextLong(50000), PenaltyUi.NO_PENALTY)
            )
        )
    }

    RoomsTheme {
        Content(
            users = users,
            solves = solves.reversed(),
            modifier = Modifier.fillMaxSize()
        )
    }
}