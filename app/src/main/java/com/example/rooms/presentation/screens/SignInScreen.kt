package com.example.rooms.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rooms.presentation.components.Logo
import com.example.rooms.presentation.components.TextField

@Composable
fun SignInScreen(
    onSignInClick: (login: String, password: String) -> Boolean,
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier
) = Column(
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .padding(20.dp)
) {
    var userName by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var isError by rememberSaveable { mutableStateOf(false) }

    Logo()
    Spacer(modifier = Modifier.height(64.dp))
    TextField(
        value = userName,
        onValueChange = { userName = it },
        placeholderText = "Username",
        isError = false
    )
    Spacer(modifier = Modifier.height(16.dp))
    TextField(
        value = password,
        onValueChange = { password = it },
        placeholderText = "Password",
        isError = false
    )
    Spacer(modifier = Modifier.height(48.dp))
    TextButton(
        contentPadding = PaddingValues(vertical = 12.dp, horizontal = 48.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        onClick = {
            isError = onSignInClick(userName, password)
        },
        modifier = Modifier.size(height = 48.dp, width = 184.dp)
    ) {
        Text(
            text = "Sign in",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
    Spacer(modifier = Modifier.height(12.dp))
    TextButton(onClick = onSignUpClick) {
        Text(
            text = "Go to sign up",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview
@Composable
private fun SignInScreenPreview() {
    SignInScreen(
        onSignInClick = { login, password -> true },
        onSignUpClick = { },
        modifier = Modifier.fillMaxSize()
    )
}