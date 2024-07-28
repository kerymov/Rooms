package com.example.rooms.presentation.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rooms.presentation.components.BaseTextField
import com.example.rooms.presentation.components.Logo
import com.example.rooms.presentation.components.PasswordTextField
import com.example.rooms.presentation.viewModels.SignInViewModel

@Composable
fun SignInScreen(
    onSignInClick: (login: String, password: String) -> Unit,
    onSignUpClick: () -> Unit,
    onSignInSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    signInViewModel: SignInViewModel = viewModel()
) {
    val context = LocalContext.current

    val signInUiState by signInViewModel.uiState.collectAsState()

    var userName by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var isError by rememberSaveable { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
    ) {
        Logo()
        Spacer(modifier = Modifier.height(64.dp))
        BaseTextField(
            value = userName,
            onValueChange = { userName = it },
            placeholderText = "Username",
            isError = isError,
        )
        Spacer(modifier = Modifier.height(16.dp))
        PasswordTextField(
            value = password,
            onValueChange = { password = it },
            isValueVisible = isPasswordVisible,
            onValueVisibilityChange = { isPasswordVisible = !isPasswordVisible },
            placeholderText = "Password",
            isError = isError
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
                onSignInClick(userName, password)
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

    LaunchedEffect(signInUiState) {
        if (signInUiState.isSuccessful) {
            onSignInSuccess()
            val toast = Toast.makeText(
                context,
                "Success!",
                Toast.LENGTH_LONG
            )
            toast.show()
        } else {
            if (signInUiState.errorMessage == null) return@LaunchedEffect

            isError = true
            val toast = Toast.makeText(
                context,
                "Error: ${signInUiState.errorMessage}",
                Toast.LENGTH_LONG
            )
            toast.show()
        }
    }
}

@Preview
@Composable
private fun SignInScreenPreview() {
    SignInScreen(
        onSignInClick = { login, password -> },
        onSignUpClick = { },
        onSignInSuccess = { },
        modifier = Modifier.fillMaxSize()
    )
}