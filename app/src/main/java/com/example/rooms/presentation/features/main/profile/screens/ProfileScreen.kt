@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.rooms.presentation.features.main.profile.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.example.rooms.presentation.components.CenterAlignedTopBar
import com.example.rooms.presentation.features.utils.toInnerScaffoldPadding
import com.example.rooms.presentation.theme.SetSystemBarIconColors

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
) {
    SetSystemBarIconColors(
        isAppearanceLightStatusBars = false,
        isAppearanceLightNavigationBars = false
    )

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
            contentPadding = contentPadding.toInnerScaffoldPadding(),
        )

        if (shouldShowLogOutDialog) {
            SignOutAlertDialog(
                onDismiss = { shouldShowLogOutDialog = false },
                onConfirm = {
                    shouldShowLogOutDialog = false
                }
            )
        }
    }
}

@Composable
private fun Content(
    contentPadding: PaddingValues
) {

}

@Composable
private fun SignOutAlertDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) = AlertDialog(
    title = {
        Text(
            text = "Sign out",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    },
    text = {
        Text(
            text = "Do you really want to sign out?",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
    },
    onDismissRequest = onDismiss,
    confirmButton = {
        TextButton(onClick = onConfirm) {
            Text(
                text = "Sing out",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.error
            )
        }
    },
    dismissButton = {
        TextButton(onClick = onConfirm) {
            Text(
                text = "Cancel",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
)