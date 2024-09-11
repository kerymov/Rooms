package com.example.rooms.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rooms.presentation.theme.SetSystemBarIconColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CenterAlignedTopBar(
    title: String,
    navigationIcon: ImageVector? = null,
    onNavigationButtonClick: () -> Unit = { },
    actions: List<Pair<ImageVector?, () -> Unit>> = listOf(),
    scrollBehaviour: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
) {
    val containerColor = MaterialTheme.colorScheme.background
    val scrolledContainerColor = MaterialTheme.colorScheme.primary
    val contentColor = MaterialTheme.colorScheme.contentColorFor(containerColor)
    val scrolledContentColor = MaterialTheme.colorScheme.contentColorFor(scrolledContainerColor)

    if (scrollBehaviour.state.overlappedFraction == 0.0f) {
        SetSystemBarIconColors()
    } else {
        SetSystemBarIconColors(false)
    }
    val currentContentColor = if (scrollBehaviour.state.overlappedFraction == 0.0f) {
        contentColor
    } else {
        scrolledContentColor
    }


    CenterAlignedTopAppBar(
        title = {
            Title(text = title)
        },
        navigationIcon = {
            navigationIcon?.let { icon ->
                TopBarIconButton(icon) {
                    onNavigationButtonClick()
                }
            }
        },
        actions = {
            actions.forEach { actionItem ->
                actionItem.first?.let { icon ->
                    TopBarIconButton(icon = icon) {
                        actionItem.second()
                    }
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = containerColor,
            scrolledContainerColor = scrolledContainerColor,
            navigationIconContentColor = currentContentColor,
            titleContentColor = currentContentColor,
            actionIconContentColor = currentContentColor
        ),
        scrollBehavior = scrollBehaviour
    )
}

@Composable
private fun Title(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
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
            modifier = Modifier.size(30.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun TopBarPreview() {
    CenterAlignedTopBar(
        title = "Rooms",
        navigationIcon = null,
        onNavigationButtonClick = { },
        actions = listOf(Icons.Filled.Add to { })
    )
}