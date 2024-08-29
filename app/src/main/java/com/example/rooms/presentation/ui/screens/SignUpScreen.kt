package com.example.rooms.presentation.ui.screens

import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.rooms.presentation.ui.components.BaseTextField
import com.example.rooms.presentation.ui.components.LoadingScreen
import com.example.rooms.presentation.ui.components.Logo
import com.example.rooms.presentation.ui.components.PasswordTextField
import com.example.rooms.presentation.ui.navigation.Screen
import com.example.rooms.presentation.ui.viewModels.SignUpUiState
import com.example.rooms.presentation.ui.viewModels.SignUpViewModel

@Composable
fun SignUpScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    signUpViewModel: SignUpViewModel = viewModel()
) {
    val context = LocalContext.current

    val uiState by signUpViewModel.uiState.collectAsState()

    var login by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var repeatedPassword by rememberSaveable { mutableStateOf("") }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var isRepeatedPasswordVisible by rememberSaveable { mutableStateOf(false) }

    var isLoading by rememberSaveable { mutableStateOf(false) }
    var isError by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is SignUpUiState.Success -> {
                isLoading = false
                isError = false

                val toast = Toast.makeText(
                    context,
                    "Success!",
                    Toast.LENGTH_SHORT
                )
                toast.show()

                navController.navigate(Screen.ROOMS.name)
            }

            is SignUpUiState.Error -> {
                isLoading = false
                isError = true
                val toast = Toast.makeText(
                    context,
                    state.message,
                    Toast.LENGTH_LONG
                )
                toast.show()
            }

            is SignUpUiState.Loading -> {
                isLoading = true
                isError = false
            }

            is SignUpUiState.None -> {
                isLoading = false
                isError = false
            }
        }
    }

    if (isLoading) {
        LoadingScreen()
    }

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
            value = login,
            onValueChange = { login = it },
            placeholderText = "Username",
            isError = false
        )
        Spacer(modifier = Modifier.height(16.dp))
        PasswordTextField(
            value = password,
            onValueChange = { password = it },
            isValueVisible = isPasswordVisible,
            onValueVisibilityChange = { isPasswordVisible = !isPasswordVisible },
            placeholderText = "Password",
            isError = isError,
        )
        Spacer(modifier = Modifier.height(16.dp))
        PasswordTextField(
            value = repeatedPassword,
            onValueChange = { repeatedPassword = it },
            isValueVisible = isRepeatedPasswordVisible,
            onValueVisibilityChange = { isRepeatedPasswordVisible = !isRepeatedPasswordVisible },
            placeholderText = "Repeat password",
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
                isError = password != repeatedPassword
                if (isError) return@TextButton

                signUpViewModel.signUp(login, password, repeatedPassword)
            },
            modifier = Modifier.size(height = 48.dp, width = 184.dp)
        ) {
            Text(
                text = "Sign up",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        TextButton(
            onClick = { navController.navigate(Screen.SIGN_IN.name) }
        ) {
            Text(
                text = "Go to sign in",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview
@Composable
private fun SignUpScreenPreview() {
    SignUpScreen(
        navController = rememberNavController(),
        modifier = Modifier.fillMaxSize()
    )
}