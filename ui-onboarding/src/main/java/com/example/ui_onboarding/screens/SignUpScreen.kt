package com.example.ui_onboarding.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ui_core.components.BaseTextField
import com.example.ui_core.components.CircularLoadingIndicator
import com.example.ui_core.components.ErrorCard
import com.example.ui_core.components.Logo
import com.example.ui_core.components.PasswordTextField
import com.example.ui_core.theme.RoomsTheme
import com.example.ui_core.theme.SetSystemBarIconColors
import com.example.ui_onboarding.utils.TextFieldHandler
import com.example.ui_onboarding.viewModels.AuthUiState
import com.example.ui_onboarding.viewModels.AuthViewModel

@Composable
fun SignUpScreen(
    onSignUpSuccess: () -> Unit,
    onGoToSignInClick: () -> Unit,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    SetSystemBarIconColors()

    val uiState by authViewModel.uiState.collectAsState()

    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var repeatedPassword by rememberSaveable { mutableStateOf("") }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var isRepeatedPasswordVisible by rememberSaveable { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    val invalidFields = remember { mutableStateListOf<Field>() }

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is AuthUiState.Success -> {
                errorMessage = null

                onSignUpSuccess()
            }
            is AuthUiState.Error -> {
                errorMessage = state.message
                invalidFields.addAll(
                    listOf(Field.PASSWORD, Field.REPEAT_PASSWORD, Field.REPEAT_PASSWORD)
                )
            }
            else -> {
                errorMessage = null
            }
        }
    }

    if (uiState is AuthUiState.Loading) {
        CircularLoadingIndicator()
    }

    val usernameTextFieldHandler = TextFieldHandler(
        value = username,
        onValueChange = {
            username = it
            errorMessage = null
            invalidFields.remove(Field.USERNAME)
        }
    )
    val passwordTextFieldHandler = TextFieldHandler(
        value = password,
        onValueChange = {
            password = it
            errorMessage = null
            invalidFields.remove(Field.PASSWORD)
        },
        isTextVisible = isPasswordVisible,
        onVisibilityChange = { isPasswordVisible = !isPasswordVisible }
    )
    val repeatedPasswordTextFieldHandler = TextFieldHandler(
        value = repeatedPassword,
        onValueChange = {
            repeatedPassword = it
            errorMessage = null
            invalidFields.remove(Field.REPEAT_PASSWORD)
        },
        isTextVisible = isRepeatedPasswordVisible,
        onVisibilityChange = { isRepeatedPasswordVisible = !isRepeatedPasswordVisible }
    )

    val onSignUpClick = {
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
            repeatedPassword.isBlank() -> {
                invalidFields.add(Field.REPEAT_PASSWORD)
                errorMessage = Field.REPEAT_PASSWORD.onEmptyErrorMessage
            }
            password != repeatedPassword -> {
                invalidFields.addAll(listOf(Field.PASSWORD, Field.REPEAT_PASSWORD))
                errorMessage = "Passwords do not match"
            }
        }

        if (errorMessage.isNullOrBlank()) {
            authViewModel.signUp(username, password, repeatedPassword)
        }
    }

    SignUpView(
        usernameTextFieldHandler = usernameTextFieldHandler,
        passwordTextFieldHandler = passwordTextFieldHandler,
        repeatedPasswordTextFieldHandler = repeatedPasswordTextFieldHandler,
        onSignUpClick = onSignUpClick,
        onGoToSignInClick = onGoToSignInClick,
        errorMessage = errorMessage,
        invalidFields = invalidFields,
        modifier = modifier,
    )
}

@Composable
private fun SignUpView(
    usernameTextFieldHandler: TextFieldHandler,
    passwordTextFieldHandler: TextFieldHandler,
    repeatedPasswordTextFieldHandler: TextFieldHandler,
    onSignUpClick: () -> Unit,
    onGoToSignInClick: () -> Unit,
    errorMessage: String?,
    invalidFields: List<Field>,
    modifier: Modifier = Modifier,
) {
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
        Content(
            usernameTextFieldHandler = usernameTextFieldHandler,
            passwordTextFieldHandler = passwordTextFieldHandler,
            repeatedPasswordTextFieldHandler = repeatedPasswordTextFieldHandler,
            onSignUpClick = onSignUpClick,
            onGoToSignInClick = onGoToSignInClick,
            invalidFields = invalidFields,
            modifier = modifier.imePadding()
        )

        if (!errorMessage.isNullOrBlank()) {
            ErrorCard(
                text = errorMessage,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
private fun Content(
    usernameTextFieldHandler: TextFieldHandler,
    passwordTextFieldHandler: TextFieldHandler,
    repeatedPasswordTextFieldHandler: TextFieldHandler,
    onSignUpClick: () -> Unit,
    onGoToSignInClick: () -> Unit,
    invalidFields: List<Field>,
    modifier: Modifier = Modifier,
) = Column(
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

    TextFields(
        usernameTextFieldHandler = usernameTextFieldHandler,
        passwordTextFieldHandler = passwordTextFieldHandler,
        repeatedPasswordTextFieldHandler = repeatedPasswordTextFieldHandler,
        invalidFields = invalidFields
    )

    Spacer(modifier = Modifier.height(48.dp))

    Buttons(
        onSignUpClick = onSignUpClick,
        onGoToSignInClick = onGoToSignInClick
    )
}

@Composable
private fun TextFields(
    usernameTextFieldHandler: TextFieldHandler,
    passwordTextFieldHandler: TextFieldHandler,
    repeatedPasswordTextFieldHandler: TextFieldHandler,
    invalidFields: List<Field>,
) = Column(
    verticalArrangement = Arrangement.spacedBy(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
) {
    BaseTextField(
        value = usernameTextFieldHandler.value,
        onValueChange = usernameTextFieldHandler.onValueChange,
        placeholderText = Field.USERNAME.placeholder,
        isError = Field.USERNAME in invalidFields,
        imeAction = ImeAction.Next,
    )
    PasswordTextField(
        value = passwordTextFieldHandler.value,
        onValueChange = passwordTextFieldHandler.onValueChange,
        isValueVisible = passwordTextFieldHandler.isTextVisible,
        onValueVisibilityChange = passwordTextFieldHandler.onVisibilityChange,
        placeholderText = Field.PASSWORD.placeholder,
        isError = Field.PASSWORD in invalidFields,
        imeAction = ImeAction.Next,
    )
    PasswordTextField(
        value = repeatedPasswordTextFieldHandler.value,
        onValueChange = repeatedPasswordTextFieldHandler.onValueChange,
        isValueVisible = repeatedPasswordTextFieldHandler.isTextVisible,
        onValueVisibilityChange = repeatedPasswordTextFieldHandler.onVisibilityChange,
        placeholderText = Field.REPEAT_PASSWORD.placeholder,
        isError = Field.REPEAT_PASSWORD in invalidFields,
        imeAction = ImeAction.Done
    )
}

@Composable
private fun Buttons(
    onSignUpClick: () -> Unit,
    onGoToSignInClick: () -> Unit,
) = Column(
    horizontalAlignment = Alignment.CenterHorizontally,
) {
    TextButton(
        contentPadding = PaddingValues(vertical = 12.dp, horizontal = 48.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        onClick = onSignUpClick,
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
        onClick = onGoToSignInClick
    ) {
        Text(
            text = "Go to sign in",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview(name = "Default")
@Composable
private fun SignUpScreenPreview() {
    val usernameTextFieldHandler = TextFieldHandler(
        value = "",
        onValueChange = { }
    )
    val passwordTextFieldHandler = TextFieldHandler(
        value = "",
        onValueChange = { },
        isTextVisible = false,
        onVisibilityChange = { }
    )
    val repeatedPasswordTextFieldHandler = TextFieldHandler(
        value = "",
        onValueChange = { },
        isTextVisible = false,
        onVisibilityChange = { }
    )

    RoomsTheme {
        SignUpView(
            usernameTextFieldHandler = usernameTextFieldHandler,
            passwordTextFieldHandler = passwordTextFieldHandler,
            repeatedPasswordTextFieldHandler = repeatedPasswordTextFieldHandler,
            onSignUpClick = { },
            onGoToSignInClick = { },
            errorMessage = null,
            invalidFields = listOf()
        )
    }
}

@Preview(name = "Error")
@Composable
private fun SignUpScreenErrorPreview() {
    val usernameTextFieldHandler = TextFieldHandler(
        value = "username",
        onValueChange = { }
    )
    val passwordTextFieldHandler = TextFieldHandler(
        value = "password",
        onValueChange = { },
        isTextVisible = false,
        onVisibilityChange = { }
    )
    val repeatedPasswordTextFieldHandler = TextFieldHandler(
        value = "password",
        onValueChange = { },
        isTextVisible = false,
        onVisibilityChange = { }
    )

    RoomsTheme {
        SignUpView(
            usernameTextFieldHandler = usernameTextFieldHandler,
            passwordTextFieldHandler = passwordTextFieldHandler,
            repeatedPasswordTextFieldHandler = repeatedPasswordTextFieldHandler,
            onSignUpClick = { },
            onGoToSignInClick = { },
            errorMessage = "Provided username already registered",
            invalidFields = listOf(Field.USERNAME, Field.PASSWORD, Field.REPEAT_PASSWORD)
        )
    }
}