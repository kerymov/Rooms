package com.kerymov.rooms.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomNavigationItem(
    val title: String,
    val route: Screen,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
) {
    ROOMS(
        title = "Rooms",
        route = Main.Rooms,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
    ),
    PROFILE(
        title = "Profile",
        route = Main.Profile,
        selectedIcon = Icons.Filled.AccountCircle,
        unselectedIcon = Icons.Outlined.AccountCircle,
    ),
}

