package com.example.rooms.presentation.features.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavHostController,
    factory: ViewModelProvider.Factory? = null
): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel(factory = factory)
    val parentEntry = remember(key1 = this) {
        navController.getBackStackEntry(navGraphRoute)
    }

    return viewModel(viewModelStoreOwner = parentEntry, factory = factory)
}

@Composable
fun PaddingValues.toOuterScaffoldPadding(
    layoutDirection: LayoutDirection = LayoutDirection.Ltr
) = PaddingValues(
    top = 0.dp,
    bottom = this.calculateBottomPadding(),
    start = this.calculateStartPadding(layoutDirection = layoutDirection),
    end = this.calculateEndPadding(layoutDirection = layoutDirection)
)

@Composable
fun PaddingValues.toInnerScaffoldPadding(
    layoutDirection: LayoutDirection = LayoutDirection.Ltr
) = PaddingValues(
    top = this.calculateTopPadding(),
    bottom = 0.dp,
    start = this.calculateStartPadding(layoutDirection = layoutDirection),
    end = this.calculateEndPadding(layoutDirection = layoutDirection)
)