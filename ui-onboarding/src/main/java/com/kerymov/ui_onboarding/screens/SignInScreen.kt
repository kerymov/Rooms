package com.kerymov.ui_onboarding.screens

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
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kerymov.ui_core.components.BaseTextField
import com.kerymov.ui_core.components.CircularLoadingIndicator
import com.kerymov.ui_core.components.ErrorCard
import com.kerymov.ui_core.components.Logo
import com.kerymov.ui_core.components.PasswordTextField
import com.kerymov.ui_core.theme.RoomsTheme
import com.kerymov.ui_core.theme.SetSystemBarIconColors
import com.kerymov.ui_onboarding.utils.TextFieldHandler
import com.kerymov.ui_onboarding.viewModels.AuthUiState
import com.kerymov.ui_onboarding.viewModels.AuthViewModel

@Composable
fun SignInScreen(
    onSignInSuccess: () -> Unit,
    onGoToSignUpClick: () -> Unit,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    SetSystemBarIconColors()

    val uiState by authViewModel.uiState.collectAsState()

    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    val invalidFields = remember { mutableStateListOf<Field>() }

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is AuthUiState.Success -> {
                errorMessage = null

                onSignInSuccess()
            }
            is AuthUiState.Error -> {
                errorMessage = state.message
                invalidFields.addAll(
                    listOf(Field.USERNAME, Field.PASSWORD)
                )
            }
            else -> {
                errorMessage = null
            }
        }
    }

    if (uiState is AuthUiState.Loading) {
        CircularLoadingIndicator(
            modifier = Modifier.testTag("loading_indicator")
        )
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

    val onSignInClick = {
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

        if (errorMessage.isNullOrBlank()) {
            authViewModel.signIn(login = username, password = password)
        }
    }

    SignInView(
        usernameTextFieldHandler = usernameTextFieldHandler,
        passwordTextFieldHandler = passwordTextFieldHandler,
        onSignInClick = onSignInClick,
        onGoToSignUpClick = onGoToSignUpClick,
        errorMessage = errorMessage,
        invalidFields = invalidFields,
        modifier = modifier
    )
}

@Composable
private fun SignInView(
    usernameTextFieldHandler: TextFieldHandler,
    passwordTextFieldHandler: TextFieldHandler,
    onSignInClick: () -> Unit,
    onGoToSignUpClick: () -> Unit,
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
            }
            .testTag("sign_in_background"),
        contentAlignment = Alignment.Center
    ) {
        Content(
            usernameTextFieldHandler = usernameTextFieldHandler,
            passwordTextFieldHandler = passwordTextFieldHandler,
            onSignInClick = onSignInClick,
            onGoToSignUpClick = onGoToSignUpClick,
            invalidFields = invalidFields,
            modifier = modifier.imePadding()
        )

        if (!errorMessage.isNullOrBlank()) {
            ErrorCard(
                text = errorMessage,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .testTag("error_card")
            )
        }
    }
}

@Composable
private fun Content(
    usernameTextFieldHandler: TextFieldHandler,
    passwordTextFieldHandler: TextFieldHandler,
    onSignInClick: () -> Unit,
    onGoToSignUpClick: () -> Unit,
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
    Logo(Modifier.testTag("logo"))

    Spacer(modifier = Modifier.height(64.dp))

    TextFields(
        usernameTextFieldHandler = usernameTextFieldHandler,
        passwordTextFieldHandler = passwordTextFieldHandler,
        invalidFields = invalidFields
    )

    Spacer(modifier = Modifier.height(48.dp))

    Buttons(
        onSignInClick = onSignInClick,
        onGoToSignUpClick = onGoToSignUpClick
    )
}

@Composable
private fun TextFields(
    usernameTextFieldHandler: TextFieldHandler,
    passwordTextFieldHandler: TextFieldHandler,
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
        modifier = Modifier
            .testTag("username_field")
            .semantics {
                contentDescription = if (Field.USERNAME in invalidFields) {
                    "Username field with error"
                } else {
                    "Username field"
                }
            }
    )

    PasswordTextField(
        value = passwordTextFieldHandler.value,
        onValueChange = passwordTextFieldHandler.onValueChange,
        isValueVisible = passwordTextFieldHandler.isTextVisible,
        onValueVisibilityChange = passwordTextFieldHandler.onVisibilityChange,
        placeholderText = Field.PASSWORD.placeholder,
        isError = Field.PASSWORD in invalidFields,
        imeAction = ImeAction.Done,
        modifier = Modifier
            .testTag("password_field")
            .semantics {
                contentDescription = if (Field.PASSWORD in invalidFields) {
                    "Password field with error"
                } else {
                    "Password field"
                }
            }
    )
}

@Composable
private fun Buttons(
    onSignInClick: () -> Unit,
    onGoToSignUpClick: () -> Unit,
) = Column(
    horizontalAlignment = Alignment.CenterHorizontally,
) {
    Button(
        contentPadding = PaddingValues(vertical = 12.dp, horizontal = 48.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        onClick = onSignInClick,
        modifier = Modifier
            .size(height = 48.dp, width = 184.dp)
            .testTag("sign_in_button")
    ) {
        Text(
            text = "Sign in",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
    Spacer(modifier = Modifier.height(12.dp))
    TextButton(
        onClick = onGoToSignUpClick,
        modifier = Modifier.testTag("go_to_sign_up_button")
    ) {
        Text(
            text = "Go to sign up",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview(name = "Default")
@Composable
private fun SignInScreenPreview() {
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

    RoomsTheme {
        SignInView(
            usernameTextFieldHandler = usernameTextFieldHandler,
            passwordTextFieldHandler = passwordTextFieldHandler,
            onSignInClick = { },
            onGoToSignUpClick = { },
            errorMessage = null,
            invalidFields = listOf()
        )
    }
}

@Preview(name = "Error")
@Composable
private fun SignInScreenErrorPreview() {
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

    RoomsTheme {
        SignInView(
            usernameTextFieldHandler = usernameTextFieldHandler,
            passwordTextFieldHandler = passwordTextFieldHandler,
            onSignInClick = { },
            onGoToSignUpClick = { },
            errorMessage = "Invalid username",
            invalidFields = listOf(Field.USERNAME, Field.PASSWORD)
        )
    }
}