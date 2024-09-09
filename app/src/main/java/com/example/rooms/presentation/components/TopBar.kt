package com.example.rooms.presentation.components

import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CenterAlignedTopBar(
    title: String,
    navigationIcon: ImageVector? = null,
    onNavigationButtonClick: () -> Unit = { },
    actions: List<Pair<ImageVector?, () -> Unit>> = listOf(),
    scrollBehaviour: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
) {
    val containerColor = MaterialTheme.colorScheme.primary
    val contentColor = MaterialTheme.colorScheme.contentColorFor(containerColor)

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
            scrolledContainerColor = containerColor,
            navigationIconContentColor = contentColor,
            titleContentColor = contentColor,
            actionIconContentColor = contentColor
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

@Preview
@Composable
private fun TopBarPreview() {
//    CenterAlignedTopBar(
//        title = "Rooms",
//        navigationIcon = R.drawable.arrow_back,
//        actionIcon = R.drawable.add,
//        onNavigationButtonClick = { },
//        onActionButtonClick = { },
//        modifier = Modifier.fillMaxWidth()
//    )
}