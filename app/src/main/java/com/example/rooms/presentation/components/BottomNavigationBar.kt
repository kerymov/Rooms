package com.example.rooms.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.rooms.presentation.navigation.NavModule

enum class BottomNavigationItem(
    val title: String,
    val destination: NavModule.Submodule,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
) {
    ROOMS(
        title = "Rooms",
        destination = NavModule.Main.Rooms,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
    ),
    PROFILE(
        title = "Profile",
        destination = NavModule.Main.Profile,
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
    ),
}

@Composable
fun BottomNavigationBar(
    items: List<BottomNavigationItem>,
    onNavItemClick: (BottomNavigationItem) -> Unit,
) {
    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItemIndex == index,
                icon = {
                    Icon(
                        imageVector = if (selectedItemIndex == index) {
                            item.selectedIcon
                        } else {
                            item.unselectedIcon
                        },
                        contentDescription = item.title
                    )
                },
                onClick = {
                    selectedItemIndex = index
                    onNavItemClick(item)
                },
                label = {
                    Text(
                        text = item.title,
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