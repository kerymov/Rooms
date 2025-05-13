package com.example.rooms.presentation.features

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
