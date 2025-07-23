package com.kerymov.ui_onboarding

import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.isNotFocused
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kerymov.ui_core.theme.RoomsTheme
import com.kerymov.ui_onboarding.screens.Field
import com.kerymov.ui_onboarding.screens.SignInScreen
import com.kerymov.ui_onboarding.viewModels.AuthUiState
import com.kerymov.ui_onboarding.viewModels.AuthViewModel
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
class SignInScreenInstrumentedTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var mockAuthViewModel: AuthViewModel
    private lateinit var uiStateFlow: MutableStateFlow<AuthUiState>
    private lateinit var onSignInSuccess: () -> Unit
    private lateinit var onGoToSignUpClick: () -> Unit

    @Before
    fun setUp() {
        mockAuthViewModel = mockk(relaxed = true)
        uiStateFlow = MutableStateFlow(AuthUiState.None)
        onSignInSuccess = mockk(relaxed = true)
        onGoToSignUpClick = mockk(relaxed = true)

        every { mockAuthViewModel.uiState } returns uiStateFlow
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun signInScreen_initialState_displaysAllComponents() {
        composeTestRule.setContent {
            RoomsTheme {
                SignInScreen(
                    onSignInSuccess = onSignInSuccess,
                    onGoToSignUpClick = onGoToSignUpClick,
                    authViewModel = mockAuthViewModel
                )
            }
        }

        with(composeTestRule) {
            onNodeWithTag("logo").assertIsDisplayed()
            onNodeWithTag("username_field").assertIsDisplayed()
            onNodeWithTag("password_field").assertIsDisplayed()
            onNodeWithTag("sign_in_button").assertIsDisplayed()
            onNodeWithTag("go_to_sign_up_button").assertIsDisplayed()
        }
    }

    @Test
    fun signInScreen_enterUserInput_fieldsArePopulated() {
        composeTestRule.setContent {
            RoomsTheme {
                SignInScreen(
                    onSignInSuccess = onSignInSuccess,
                    onGoToSignUpClick = onGoToSignUpClick,
                    authViewModel = mockAuthViewModel
                )
            }
        }

        val testUsername = "testuser"
        val testPassword = "password123"

        composeTestRule.onRoot(useUnmergedTree = true).printToLog("semantics_tree")

        with(composeTestRule) {
            onNodeWithTag("username_field", useUnmergedTree = true)
                .performTextInput(testUsername)
            onNodeWithTag("password_field", useUnmergedTree = true)
                .performTextInput(testPassword)

            onAllNodesWithContentDescription("Show password", useUnmergedTree = true)
                .onFirst()
                .performClick()

            onNodeWithTag("username_field", useUnmergedTree = true)
                .assertTextEquals(testUsername)
            onNodeWithTag("password_field", useUnmergedTree = true)
                .assertTextEquals(testPassword)
        }
    }

    @Test
    fun signInScreen_clickSignInWithValidData_callsViewModel() {
        composeTestRule.setContent {
            RoomsTheme {
                SignInScreen(
                    onSignInSuccess = onSignInSuccess,
                    onGoToSignUpClick = onGoToSignUpClick,
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

            onNodeWithTag("sign_in_button")
                .performClick()
        }

        verify { mockAuthViewModel.signIn(testUsername, testPassword) }
    }

    @Test
    fun signInScreen_clickSignInWithEmptyUsername_showsError() {
        composeTestRule.setContent {
            RoomsTheme {
                SignInScreen(
                    onSignInSuccess = onSignInSuccess,
                    onGoToSignUpClick = onGoToSignUpClick,
                    authViewModel = mockAuthViewModel
                )
            }
        }

        with(composeTestRule) {
            onNodeWithTag("sign_in_button")
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
    fun signInScreen_clickSignInWithEmptyPassword_showsError() {
        composeTestRule.setContent {
            RoomsTheme {
                SignInScreen(
                    onSignInSuccess = onSignInSuccess,
                    onGoToSignUpClick = onGoToSignUpClick,
                    authViewModel = mockAuthViewModel
                )
            }
        }

        with(composeTestRule) {
            onNodeWithTag("username_field")
                .performTextInput("testuser")

            onNodeWithTag("sign_in_button")
                .performClick()

            onNodeWithTag("error_card")
                .assertIsDisplayed()
            onNodeWithText(Field.PASSWORD.onEmptyErrorMessage)
                .assertIsDisplayed()
        }
    }

    @Test
    fun signInScreen_clickGoToSignUp_triggersCallback() {
        composeTestRule.setContent {
            RoomsTheme {
                SignInScreen(
                    onSignInSuccess = onSignInSuccess,
                    onGoToSignUpClick = onGoToSignUpClick,
                    authViewModel = mockAuthViewModel
                )
            }
        }

        composeTestRule.onNodeWithTag("go_to_sign_up_button")
            .performClick()

        verify { onGoToSignUpClick() }
    }

    @Test
    fun signInScreen_loadingState_showsLoadingIndicator() {
        uiStateFlow.value = AuthUiState.Loading

        composeTestRule.setContent {
            RoomsTheme {
                SignInScreen(
                    onSignInSuccess = onSignInSuccess,
                    onGoToSignUpClick = onGoToSignUpClick,
                    authViewModel = mockAuthViewModel
                )
            }
        }

        composeTestRule.onNodeWithTag("loading_indicator")
            .assertIsDisplayed()
    }

    @Test
    fun signInScreen_successState_triggersSuccessCallback() {
        composeTestRule.setContent {
            RoomsTheme {
                SignInScreen(
                    onSignInSuccess = onSignInSuccess,
                    onGoToSignUpClick = onGoToSignUpClick,
                    authViewModel = mockAuthViewModel
                )
            }
        }

        uiStateFlow.value = AuthUiState.Success

        composeTestRule.waitForIdle()

        verify { onSignInSuccess() }
    }

    @Test
    fun signInScreen_errorState_showsErrorMessage() {
        val errorMessage = "Invalid username"
        uiStateFlow.value = AuthUiState.Error(null, errorMessage)

        composeTestRule.setContent {
            RoomsTheme {
                SignInScreen(
                    onSignInSuccess = onSignInSuccess,
                    onGoToSignUpClick = onGoToSignUpClick,
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
    fun signInScreen_tapOutsideFields_clearsFocus() {
        composeTestRule.setContent {
            RoomsTheme {
                SignInScreen(
                    onSignInSuccess = onSignInSuccess,
                    onGoToSignUpClick = onGoToSignUpClick,
                    authViewModel = mockAuthViewModel
                )
            }
        }

        with(composeTestRule) {
            onNodeWithTag("username_field")
                .performClick()
            onNodeWithTag("sign_in_background")
                .performClick()

            onNodeWithTag("username_field")
                .onChildren()
                .assertAll(isNotFocused())
            onNodeWithTag("password_field")
                .onChildren()
                .assertAll(isNotFocused())
        }
    }

    @Test
    fun signInScreen_inputValidation_clearsErrorsOnInput() {
        composeTestRule.setContent {
            RoomsTheme {
                SignInScreen(
                    onSignInSuccess = onSignInSuccess,
                    onGoToSignUpClick = onGoToSignUpClick,
                    authViewModel = mockAuthViewModel
                )
            }
        }

        with(composeTestRule) {
            onNodeWithTag("sign_in_button")
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
