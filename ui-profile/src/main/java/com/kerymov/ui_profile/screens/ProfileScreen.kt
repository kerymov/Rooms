package com.kerymov.ui_profile.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kerymov.ui_core.components.CustomAlertDialog
import com.kerymov.ui_core.components.CustomAlertDialogDefaults
import com.kerymov.ui_core.components.Divider
import com.kerymov.ui_profile.viewModels.ProfileViewModel

@Composable
fun ProfileScreen(
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    var shouldShowLogOutDialog by rememberSaveable { mutableStateOf(false) }

    Content(
        username = uiState.user?.username ?: "user",
        onRecordsClick = { },
        onAllResultsClick = { },
        onSignOutClick = { shouldShowLogOutDialog = true },
        modifier = modifier,
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

@Composable
private fun Content(
    username: String,
    onRecordsClick: () -> Unit,
    onAllResultsClick: () -> Unit,
    onSignOutClick: () -> Unit,
    modifier: Modifier = Modifier
) = Box(
    modifier = modifier.fillMaxSize()
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        UserInfo(username)
//        Section(
//            title = "Results",
//            buttons = listOf(
//                ProfileButton("Records") { onRecordsClick() },
//                ProfileButton("All results") { onAllResultsClick() }
//            )
//        )
        Section(
            title = "Account",
            buttons = listOf(
                ProfileButton("Sign out") { onSignOutClick() }
            ),
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError
        )
    }
}

@Composable
private fun UserInfo(
    username: String
) = Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.primary
    ),
    modifier = Modifier.fillMaxWidth()
) {
    Text(
        text = "Hello, $username",
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(24.dp)
    )
}

@Composable
private fun Section(
    title: String,
    buttons: List<ProfileButton>,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface
) = Column(
    verticalArrangement = Arrangement.spacedBy(16.dp)
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onBackground
    )

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(containerColor)
            .fillMaxWidth()
    ) {
        buttons.forEachIndexed { index, button ->
            ProfileButton(
                button,
                contentColor
            )

            if (index != buttons.lastIndex) {
                Divider(
                    orientation = Orientation.Horizontal,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )
            }
        }
    }
}

@Composable
private fun ProfileButton(
    button: ProfileButton,
    contentColor: Color = MaterialTheme.colorScheme.onBackground
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { button.onClick() }
            .padding(vertical = 8.dp, horizontal = 24.dp)
    ) {
        Text(
            text = button.title,
            color = contentColor,
            style = MaterialTheme.typography.bodyLarge
        )

        Icon(
            imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
            contentDescription = "Go to ${button.title}",
            tint = contentColor
        )
    }
}

data class ProfileButton(
    val title: String,
    val onClick: () -> Unit
)
