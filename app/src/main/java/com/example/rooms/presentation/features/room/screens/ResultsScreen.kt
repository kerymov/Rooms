package com.example.rooms.presentation.features.main.rooms.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.rooms.R
import com.example.rooms.presentation.SolvesRepository
import com.example.rooms.presentation.components.CenterAlignedTopBar
import com.example.rooms.presentation.theme.ChangeSystemBarsColors
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ResultsScreen(
    roomName: String,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    ChangeSystemBarsColors(
        systemBarColor = MaterialTheme.colorScheme.background,
        isAppearanceLightStatusBars = true,
        navigationBarColor = MaterialTheme.colorScheme.background,
        isAppearanceLightNavigationBars = true
    )

    Scaffold(
        topBar = {
//            CenterAlignedTopBar(
//                title = "Results",
//                navigationIcon = R.drawable.arrow_back,
//                actionIcon = null,
//                onNavigationButtonClick = { navController.popBackStack() },
//                onActionButtonClick = {}
//            )
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
            modifier = modifier.fillMaxSize()
        ) {
            val solvesByUser = SolvesRepository.getUserSolvesFromRoom(roomName = roomName)
            items(solvesByUser.keys.toList()) { username ->
                UserCard(
                    best = formatTime(SolvesRepository.getBestResult(username, roomName)),
                    ao5 = SolvesRepository.getLastAverageOf(5, username, roomName)
                        ?.let { value -> formatTime(value) },
                    ao12 = SolvesRepository.getLastAverageOf(12, username, roomName)
                        ?.let { value -> formatTime(value) },
                    ao100 = SolvesRepository.getLastAverageOf(100, username, roomName)
                        ?.let { value -> formatTime(value) },
                    mean = formatTime(SolvesRepository.getMeanOfAllSolves(username, roomName)),
                    username = username,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}

@Composable
private fun UserCard(
    best: String?,
    ao5: String?,
    ao12: String?,
    ao100: String?,
    mean: String?,
    username: String,
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
    Column(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
    ) {
        StatisticItem(label = "BEST", value = best)
        StatisticItem(label = "AO5", value = ao5)
        StatisticItem(label = "AO12", value = ao12)
        StatisticItem(label = "AO100", value = ao100)
        StatisticItem(label = "MEAN", value = mean)
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
private fun StatisticItem(label: String, value: String?) = Text(
    text = "$label: ${value ?: "-"}",
    style = MaterialTheme.typography.bodyMedium,
    color = MaterialTheme.colorScheme.onBackground,
    maxLines = 1,
    overflow = TextOverflow.Ellipsis
)

private enum class Duration(val timeInMills: Long, val formatPattern: String) {
    INITIAL_AND_MORE(0, "s.SS"),
    TEN_SECONDS_AND_MORE(10000, "ss.SS"),
    ONE_MINUTE_AND_MORE(60000, "m:ss.SS"),
    TEN_MINUTES_AND_MORE(600000, "mm:ss.SS"),
    ONE_HOUR_AND_MORE(3600000, "h:mm:ss.SS")
}

private fun formatTime(timeInMills: Long): String {
    val localDateTime = LocalDateTime.ofInstant(
        Instant.ofEpochMilli(timeInMills),
        ZoneId.systemDefault()
    )
    val pattern = when {
        timeInMills < Duration.TEN_SECONDS_AND_MORE.timeInMills -> Duration.INITIAL_AND_MORE.formatPattern
        timeInMills < Duration.ONE_MINUTE_AND_MORE.timeInMills -> Duration.TEN_SECONDS_AND_MORE.formatPattern
        timeInMills < Duration.TEN_MINUTES_AND_MORE.timeInMills -> Duration.ONE_MINUTE_AND_MORE.formatPattern
        timeInMills < Duration.ONE_HOUR_AND_MORE.timeInMills -> Duration.TEN_MINUTES_AND_MORE.formatPattern
        else -> Duration.ONE_HOUR_AND_MORE.formatPattern
    }
    val formatter = DateTimeFormatter.ofPattern(
        pattern,
        Locale.getDefault()
    )
    return localDateTime.format(formatter)
}