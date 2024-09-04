package com.example.rooms.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rooms.R

@Composable
fun TopBar(
    title: String,
    modifier: Modifier = Modifier,
    @DrawableRes navigationIcon: Int?,
    @DrawableRes actionIcon: Int?,
    onNavigationButtonClick: () -> Unit = { },
    onActionButtonClick: () -> Unit = { }
) = Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween,
    modifier = modifier
        .fillMaxWidth()
        .height(56.dp)
        .padding(horizontal = 20.dp)
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(48.dp)
    ) {
        navigationIcon?.let {
            NavigationButton(
                icon = it,
                onClick = onNavigationButtonClick
            )
        }
    }

    Title(text = title)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(48.dp)
    ) {
        actionIcon?.let {
            ActionButton(
                icon = it,
                onClick = onActionButtonClick
            )
        }
    }
}

@Composable
private fun Title(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        color = contentColorFor(MaterialTheme.colorScheme.background),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun NavigationButton(
    @DrawableRes icon: Int,
    onClick: () -> Unit
) {
    IconButton(onClick = { onClick() }) {
        Icon(
            imageVector = ImageVector.vectorResource(id = icon),
            contentDescription = "Navigation button",
            modifier = Modifier.size(28.dp)
        )
    }
}

@Composable
private fun ActionButton(
    @DrawableRes icon: Int,
    onClick: () -> Unit
) {
    IconButton(onClick = { onClick() }) {
        Icon(
            imageVector = ImageVector.vectorResource(id = icon),
            contentDescription = "Action button",
            modifier = Modifier.size(28.dp)
        )
    }
}

@Preview
@Composable
private fun TopBarPreview() {
    TopBar(
        title = "Rooms",
        navigationIcon = R.drawable.arrow_back,
        actionIcon = R.drawable.add,
        onNavigationButtonClick = { },
        onActionButtonClick = { },
        modifier = Modifier.fillMaxWidth()
    )
}