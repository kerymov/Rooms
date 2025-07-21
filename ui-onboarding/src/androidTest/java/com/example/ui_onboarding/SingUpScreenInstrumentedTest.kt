package com.example.ui_onboarding

import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.isNotFocused
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.ui_core.theme.RoomsTheme
import com.example.ui_onboarding.screens.Field
import com.example.ui_onboarding.screens.SignUpScreen
import com.example.ui_onboarding.viewModels.AuthUiState
import com.example.ui_onboarding.viewModels.AuthViewModel
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignUpScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var mockAuthViewModel: AuthViewModel
    private lateinit var uiStateFlow: MutableStateFlow<AuthUiState>
    private lateinit var onSignUpSuccess: () -> Unit
    private lateinit var onGoToSignInClick: () -> Unit

    @Before
    fun setUp() {
        mockAuthViewModel = mockk(relaxed = true)
        uiStateFlow = MutableStateFlow(AuthUiState.None)
        onSignUpSuccess = mockk(relaxed = true)
        onGoToSignInClick = mockk(relaxed = true)

        every { mockAuthViewModel.uiState } returns uiStateFlow
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun signUpScreen_initialState_displaysAllComponents() {
        composeTestRule.setContent {
            RoomsTheme {
                SignUpScreen(
                    onSignUpSuccess = onSignUpSuccess,
                    onGoToSignInClick = onGoToSignInClick,
                    authViewModel = mockAuthViewModel
                )
            }
        }

        with(composeTestRule) {
            onNodeWithTag("logo").assertIsDisplayed()
            onNodeWithTag("username_field").assertIsDisplayed()
            onNodeWithTag("password_field").assertIsDisplayed()
            onNodeWithTag("repeat_password_field").assertIsDisplayed()
            onNodeWithTag("sign_up_button").assertIsDisplayed()
            onNodeWithTag("go_to_sign_in_button").assertIsDisplayed()
        }
    }

    @Test
    fun signUpScreen_inputFields_haveWithPasswordVisualTransformation_byDefault() {
        composeTestRule.setContent {
            RoomsTheme {
                SignUpScreen(
                    onSignUpSuccess = onSignUpSuccess,
                    onGoToSignInClick = onGoToSignInClick,
                    authViewModel = mockAuthViewModel
                )
            }
        }

        val testPassword = "password123"
        val testRepeatPassword = "password123"

        with(composeTestRule) {
            onNodeWithTag("password_field", useUnmergedTree = true)
                .performTextInput(testPassword)
            onNodeWithTag("repeat_password_field", useUnmergedTree = true)
                .performTextInput(testRepeatPassword)

            onNodeWithTag("password_field", useUnmergedTree = true)
                .assertTextEquals("•".repeat(testPassword.length))
            onNodeWithTag("repeat_password_field", useUnmergedTree = true)
                .assertTextEquals("•".repeat(testRepeatPassword.length))
        }
    }

    @Test
    fun signUpScreen_enterUserInput_fieldsArePopulated() {
        composeTestRule.setContent {
            RoomsTheme {
                SignUpScreen(
                    onSignUpSuccess = onSignUpSuccess,
                    onGoToSignInClick = onGoToSignInClick,
                    authViewModel = mockAuthViewModel
                )
            }
        }

        val testUsername = "testuser"
        val testPassword = "password123"
        val testRepeatPassword = "password123"

        composeTestRule.onRoot(useUnmergedTree = true).printToLog("semantics_tree")

        with(composeTestRule) {
            onNodeWithTag("username_field", useUnmergedTree = true)
                .performTextInput(testUsername)
            onNodeWithTag("password_field", useUnmergedTree = true)
                .performTextInput(testPassword)
            onNodeWithTag("repeat_password_field", useUnmergedTree = true)
                .performTextInput(testRepeatPassword)

            onAllNodesWithContentDescription("Show password", useUnmergedTree = true)
                .onFirst()
                .performClick()

            onAllNodesWithContentDescription("Show password", useUnmergedTree = true)
                .onLast()
                .performClick()

            onNodeWithTag("username_field", useUnmergedTree = true)
                .assertTextEquals(testUsername)
            onNodeWithTag("password_field", useUnmergedTree = true)
                .assertTextEquals(testPassword)
            onNodeWithTag("repeat_password_field", useUnmergedTree = true)
                .assertTextEquals(testRepeatPassword)
        }
    }

    @Test
    fun signUpScreen_clickSignUpWithValidData_callsViewModel() {
        composeTestRule.setContent {
            RoomsTheme {
                SignUpScreen(
                    onSignUpSuccess = onSignUpSuccess,
                    onGoToSignInClick = onGoToSignInClick,
                    authViewModel = mockAuthViewModel
                )
            }
        }

        val testUsername = "testuser"
        val testPassword = "password123"

        with(composeTestRule) {
            onNodeWithTag("username_field")
                .performTextInput(testUsername)
            onNodeWithTag("password_field")
                .performTextInput(testPassword)
            onNodeWithTag("repeat_password_field")
                .performTextInput(testPassword)

            onNodeWithTag("sign_up_button")
                .performClick()
        }

        verify { mockAuthViewModel.signUp(testUsername, testPassword, testPassword) }
    }

    @Test
    fun signUpScreen_clickSignUpWithEmptyUsername_showsError() {
        composeTestRule.setContent {
            RoomsTheme {
                SignUpScreen(
                    onSignUpSuccess = onSignUpSuccess,
                    onGoToSignInClick = onGoToSignInClick,
                    authViewModel = mockAuthViewModel
                )
            }
        }

        with(composeTestRule) {
            onNodeWithTag("sign_up_button")
                .performClick()

            onNodeWithTag("error_card")
                .assertIsDisplayed()
            onNodeWithText(Field.USERNAME.onEmptyErrorMessage)
                .assertIsDisplayed()

            onNodeWithTag("username_field")
                .assertIsDisplayed()
        }
    }

    @Test
    fun signUpScreen_clickSignUpWithEmptyPassword_showsError() {
        composeTestRule.setContent {
            RoomsTheme {
                SignUpScreen(
                    onSignUpSuccess = onSignUpSuccess,
                    onGoToSignInClick = onGoToSignInClick,
                    authViewModel = mockAuthViewModel
                )
            }
        }

        with(composeTestRule) {
            onNodeWithTag("username_field")
                .performTextInput("testuser")

            onNodeWithTag("sign_up_button")
                .performClick()

            onNodeWithTag("error_card")
                .assertIsDisplayed()
            onNodeWithText(Field.PASSWORD.onEmptyErrorMessage)
                .assertIsDisplayed()
        }
    }

    @Test
    fun signUpScreen_clickSignUpWithMismatchedPasswords_showsError() {
        composeTestRule.setContent {
            RoomsTheme {
                SignUpScreen(
                    onSignUpSuccess = onSignUpSuccess,
                    onGoToSignInClick = onGoToSignInClick,
                    authViewModel = mockAuthViewModel
                )
            }
        }

        with(composeTestRule) {
            onNodeWithTag("username_field")
                .performTextInput("testuser")
            onNodeWithTag("password_field")
                .performTextInput("password123")
            onNodeWithTag("repeat_password_field")
                .performTextInput("different123")

            onNodeWithTag("sign_up_button")
                .performClick()

            onNodeWithTag("error_card")
                .assertIsDisplayed()
            onNodeWithText("Passwords do not match")
                .assertIsDisplayed()
        }

        verify(exactly = 0) { mockAuthViewModel.signUp(any(), any(), any()) }
    }

    @Test
    fun signUpScreen_clickGoToSignIn_triggersCallback() {
        composeTestRule.setContent {
            RoomsTheme {
                SignUpScreen(
                    onSignUpSuccess = onSignUpSuccess,
                    onGoToSignInClick = onGoToSignInClick,
                    authViewModel = mockAuthViewModel
                )
            }
        }

        composeTestRule.onNodeWithTag("go_to_sign_in_button")
            .performClick()

        verify { onGoToSignInClick() }
    }

    @Test
    fun signUpScreen_loadingState_showsLoadingIndicator() {
        uiStateFlow.value = AuthUiState.Loading

        composeTestRule.setContent {
            RoomsTheme {
                SignUpScreen(
                    onSignUpSuccess = onSignUpSuccess,
                    onGoToSignInClick = onGoToSignInClick,
                    authViewModel = mockAuthViewModel
                )
            }
        }

        composeTestRule.onNodeWithTag("loading_indicator")
            .assertIsDisplayed()
    }

    @Test
    fun signUpScreen_successState_triggersSuccessCallback() {
        composeTestRule.setContent {
            RoomsTheme {
                SignUpScreen(
                    onSignUpSuccess = onSignUpSuccess,
                    onGoToSignInClick = onGoToSignInClick,
                    authViewModel = mockAuthViewModel
                )
            }
        }

        uiStateFlow.value = AuthUiState.Success

        composeTestRule.waitForIdle()

        verify { onSignUpSuccess() }
    }

    @Test
    fun signUpScreen_errorState_showsErrorMessage() {
        val errorMessage = "Username already exists"
        uiStateFlow.value = AuthUiState.Error(null, errorMessage)

        composeTestRule.setContent {
            RoomsTheme {
                SignUpScreen(
                    onSignUpSuccess = onSignUpSuccess,
                    onGoToSignInClick = onGoToSignInClick,
                    authViewModel = mockAuthViewModel
                )
            }
        }

        with(composeTestRule) {
            onNodeWithTag("error_card")
                .assertIsDisplayed()
            onNodeWithText(errorMessage)
                .assertIsDisplayed()
        }
    }

    @Test
    fun signUpScreen_tapOutsideFields_clearsFocus() {
        composeTestRule.setContent {
            RoomsTheme {
                SignUpScreen(
                    onSignUpSuccess = onSignUpSuccess,
                    onGoToSignInClick = onGoToSignInClick,
                    authViewModel = mockAuthViewModel
                )
            }
        }

        with(composeTestRule) {
            onNodeWithTag("username_field")
                .performClick()
            onNodeWithTag("sign_up_background")
                .performClick()

            onNodeWithTag("username_field")
                .onChildren()
                .assertAll(isNotFocused())
            onNodeWithTag("password_field")
                .onChildren()
                .assertAll(isNotFocused())
            onNodeWithTag("repeat_password_field")
                .onChildren()
                .assertAll(isNotFocused())
        }
    }

    @Test
    fun signUpScreen_inputValidation_clearsErrorsOnInput() {
        composeTestRule.setContent {
            RoomsTheme {
                SignUpScreen(
                    onSignUpSuccess = onSignUpSuccess,
                    onGoToSignInClick = onGoToSignInClick,
                    authViewModel = mockAuthViewModel
                )
            }
        }

        with(composeTestRule) {
            onNodeWithTag("sign_up_button")
                .performClick()
            onNodeWithTag("error_card")
                .assertIsDisplayed()

            onNodeWithTag("username_field")
                .performTextInput("test")

            onNodeWithTag("error_card")
                .assertDoesNotExist()
        }
    }
}
