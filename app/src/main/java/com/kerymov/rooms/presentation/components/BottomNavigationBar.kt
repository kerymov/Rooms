package com.kerymov.rooms.presentation.components

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kerymov.rooms.presentation.navigation.BottomNavigationItem
import com.kerymov.ui_core.components.Divider
import com.kerymov.ui_core.theme.RoomsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar(
    items: List<Pair<BottomNavigationItem, Boolean>>,
    onNavItemClick: (item: BottomNavigationItem) -> Unit,
) {
    Column {
        Divider(
            orientation = Orientation.Horizontal,
            shape = RectangleShape,
            thickness = 2.dp,
            modifier = Modifier.fillMaxWidth()
        )

        NavigationBar(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
        ) {
            items.forEach { item ->
                val (navItem, isSelected) = item

                CompositionLocalProvider(LocalRippleConfiguration provides null) {
                    NavigationBarItem(
                        selected = isSelected,
                        icon = {
                            Icon(
                                imageVector = if (isSelected) {
                                    navItem.selectedIcon
                                } else {
                                    navItem.unselectedIcon
                                },
                                contentDescription = navItem.title,
                                modifier = Modifier.size(32.dp)
                            )
                        },
                        onClick = {
                            onNavItemClick(navItem)
                        },
                        label = {
                            Text(
                                text = navItem.title,
                                style = MaterialTheme.typography.labelLarge
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.outlineVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.outlineVariant,
                            indicatorColor = Color.Transparent,
                        ),
                        alwaysShowLabel = true,
                        interactionSource = remember { MutableInteractionSource() }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewBottomNavigationBar() {
    RoomsTheme {
        BottomNavigationBar(
            items = listOf(
                BottomNavigationItem.ROOMS to true,
                BottomNavigationItem.PROFILE to false
            ),
            onNavItemClick = { }
        )
    }
}