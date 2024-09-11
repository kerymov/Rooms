@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.rooms.presentation.features.main.profile.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rooms.presentation.components.CenterAlignedTopBar
import com.example.rooms.presentation.components.CustomAlertDialog
import com.example.rooms.presentation.components.CustomAlertDialogDefaults
import com.example.rooms.presentation.features.main.profile.viewModels.ProfileUiState
import com.example.rooms.presentation.features.main.profile.viewModels.ProfileViewModel
import com.example.rooms.presentation.features.utils.toInnerScaffoldPadding

@Composable
fun ProfileScreen(
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var shouldShowLogOutDialog by remember { mutableStateOf(false) }

    val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        topBar = {
            CenterAlignedTopBar(
                title = "Profile",
                actions = listOf(
                    Icons.AutoMirrored.Filled.Logout to {
                        shouldShowLogOutDialog = true
                    }
                ),
                scrollBehaviour = topAppBarScrollBehavior
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = contentColorFor(MaterialTheme.colorScheme.background),
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
    ) { contentPadding ->
        Content(
            uiState = uiState,
            contentPadding = contentPadding.toInnerScaffoldPadding(),
        )

        if (shouldShowLogOutDialog) {
            CustomAlertDialog(
                title = "Sign out",
                message = "Do you really want to sign out?",
                dismissButtonText = "Cancel",
                confirmButtonText = "Sign out",
                onDismiss = { shouldShowLogOutDialog = false },
                onConfirm = {
                    viewModel.signOut()
                    shouldShowLogOutDialog = false
                    onSignOut()
                },
                colors = CustomAlertDialogDefaults.alertColors(
                    confirmButtonColor = MaterialTheme.colorScheme.error
                )
            )
        }
    }
}

@Composable
private fun Content(
    uiState: ProfileUiState,
    contentPadding: PaddingValues
) = Box(
    modifier = Modifier
        .fillMaxSize()
        .padding(contentPadding)
) {
    Text(text = uiState.username ?: "Username")
}