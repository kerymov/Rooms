package com.kerymov.rooms.presentation.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.kerymov.rooms.presentation.navigation.BottomNavigationItem
import com.kerymov.ui_core.theme.RoomsTheme

@Composable
fun BottomNavigationBar(
    items: List<Pair<BottomNavigationItem, Boolean>>,
    onNavItemClick: (item: BottomNavigationItem) -> Unit,
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
    ) {
        items.forEach { item ->
            val (navItem, isSelected) = item

            NavigationBarItem(
                selected = isSelected,
                icon = {
                    Icon(
                        imageVector = if (isSelected) {
                            navItem.selectedIcon
                        } else {
                            navItem.unselectedIcon
                        },
                        contentDescription = navItem.title
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
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unselectedIconColor = MaterialTheme.colorScheme.secondaryContainer,
                    unselectedTextColor = MaterialTheme.colorScheme.secondaryContainer,
                    indicatorColor = MaterialTheme.colorScheme.surfaceTint,
                ),
                alwaysShowLabel = false
            )
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