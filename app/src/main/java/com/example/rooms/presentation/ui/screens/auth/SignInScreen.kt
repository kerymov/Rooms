package com.example.rooms.presentation.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.rooms.presentation.ui.components.BaseTextField
import com.example.rooms.presentation.ui.components.ErrorCard
import com.example.rooms.presentation.ui.components.LoadingScreen
import com.example.rooms.presentation.ui.components.Logo
import com.example.rooms.presentation.ui.components.PasswordTextField
import com.example.rooms.presentation.ui.navigation.Screen
import com.example.rooms.presentation.ui.viewModels.SignInUiState
import com.example.rooms.presentation.ui.viewModels.SignInViewModel

@Composable
fun SignInScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    signInViewModel: SignInViewModel = viewModel()
) {
    val uiState by signInViewModel.uiState.collectAsState()

    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }

    var isLoading by rememberSaveable { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val invalidFields = remember { mutableStateListOf<Field>() }

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is SignInUiState.Success -> {
                isLoading = false
                errorMessage = null

                navController.navigate(Screen.ROOMS.name)
            }

            is SignInUiState.Error -> {
                isLoading = false
                errorMessage = state.message
                invalidFields.addAll(
                    listOf(Field.USERNAME, Field.PASSWORD)
                )
            }

            is SignInUiState.Loading -> {
                isLoading = true
                errorMessage = null
            }

            is SignInUiState.None -> {
                isLoading = false
                errorMessage = null
            }
        }
    }

    if (isLoading) {
        LoadingScreen()
    }

    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Logo()
            Spacer(modifier = Modifier.height(64.dp))
            BaseTextField(
                value = username,
                onValueChange = {
                    username = it
                    errorMessage = null
                    invalidFields.remove(Field.USERNAME)
                },
                placeholderText = Field.USERNAME.placeholder,
                isError = Field.USERNAME in invalidFields,
                imeAction = ImeAction.Next
            )
            Spacer(modifier = Modifier.height(16.dp))
            PasswordTextField(
                value = password,
                onValueChange = {
                    password = it
                    errorMessage = null
                    invalidFields.remove(Field.PASSWORD)
                },
                isValueVisible = isPasswordVisible,
                onValueVisibilityChange = { isPasswordVisible = !isPasswordVisible },
                placeholderText = Field.PASSWORD.placeholder,
                isError = Field.PASSWORD in invalidFields,
                imeAction = ImeAction.Done
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
                    invalidFields.clear()

                    when {
                        username.isBlank() -> {
                            invalidFields.add(Field.USERNAME)
                            errorMessage = Field.USERNAME.onEmptyErrorMessage
                        }
                        password.isBlank() -> {
                            invalidFields.add(Field.PASSWORD)
                            errorMessage = Field.PASSWORD.onEmptyErrorMessage
                        }
                    }

                    if (!errorMessage.isNullOrBlank()) return@TextButton

                    signInViewModel.signIn(login = username, password = password)
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
            TextButton(onClick = {
                navController.navigate(Screen.SIGN_UP.name) {
                    popUpTo(0)
                }
            }) {
                Text(
                    text = "Go to sign up",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        if (!errorMessage.isNullOrBlank()) {
            ErrorCard(
                text = errorMessage ?: "Error",
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Preview
@Composable
private fun SignInScreenPreview() {
    SignInScreen(
        navController = rememberNavController(),
        modifier = Modifier.fillMaxSize()
    )
}