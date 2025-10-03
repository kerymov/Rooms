package com.kerymov.ui_core.components

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Immutable
data class TopAppBarInteractionItem(
    val icon: ImageVector,
    val onClick: () -> Unit
)

@Immutable
data class TopAppBarItem(
    val title: String,
    val navigationItem: TopAppBarInteractionItem? = null,
    val actions: List<TopAppBarInteractionItem> = emptyList(),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CenterAlignedTopBar(
    item: TopAppBarItem,
    modifier: Modifier = Modifier,
    scrollBehaviour: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
) {
    val containerColor = MaterialTheme.colorScheme.background
    val scrolledContainerColor = MaterialTheme.colorScheme.primary
    val contentColor = MaterialTheme.colorScheme.contentColorFor(containerColor)
    val scrolledContentColor = MaterialTheme.colorScheme.contentColorFor(scrolledContainerColor)

    val currentContentColor = if (scrollBehaviour.state.overlappedFraction == 0.0f) {
        contentColor
    } else {
        scrolledContentColor
    }

    CenterAlignedTopAppBar(
        title = {
            Title(text = item.title)
        },
        navigationIcon = {
            item.navigationItem?.let {
                TopBarIconButton(it.icon, it.onClick)
            }
        },
        actions = {
            item.actions.forEach { action ->
                TopBarIconButton(icon = action.icon, onClick = action.onClick)
            }
        },
        expandedHeight = TopAppBarDefaults.MediumAppBarExpandedHeight,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = containerColor,
            scrolledContainerColor = containerColor,
            navigationIconContentColor = currentContentColor,
            titleContentColor = currentContentColor,
            actionIconContentColor = currentContentColor
        ),
        scrollBehavior = scrollBehaviour,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    item: TopAppBarItem,
    modifier: Modifier = Modifier,
    scrollBehaviour: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
) {
    Column {
        val shouldShowDivider by remember {
            derivedStateOf { scrollBehaviour.state.overlappedFraction != 0.0f }
        }
        TopAppBar(
            title = {
                Title(text = item.title)
            },
            navigationIcon = {
                item.navigationItem?.let {
                    TopBarIconButton(it.icon, it.onClick)
                }
            },
            actions = {
                item.actions.forEach { action ->
                    TopBarIconButton(icon = action.icon, onClick = action.onClick)
                }
            },
            expandedHeight = TopAppBarDefaults.TopAppBarExpandedHeight,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                scrolledContainerColor = MaterialTheme.colorScheme.background,
                navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                titleContentColor = MaterialTheme.colorScheme.onBackground,
                actionIconContentColor = MaterialTheme.colorScheme.onBackground
            ),
            scrollBehavior = scrollBehaviour,
            modifier = modifier
        )

        if (shouldShowDivider) {
            Divider(
                orientation = Orientation.Horizontal,
                shape = RectangleShape,
                thickness = 2.dp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun Title(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineSmall,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun TopBarIconButton(
    icon: ImageVector,
    onClick: () -> Unit
) {
    IconButton(onClick = { onClick() }) {
        Icon(
            imageVector = icon,
            contentDescription = "Navigation button",
            modifier = Modifier.size(32.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun TopBarPreview() {
    CenterAlignedTopBar(
        item = TopAppBarItem(
            title = "Rooms",
            navigationItem = TopAppBarInteractionItem(Icons.Filled.Add) { },
        )
    )
}