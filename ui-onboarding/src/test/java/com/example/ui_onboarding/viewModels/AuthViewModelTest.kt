package com.example.ui_onboarding.viewModels

import app.cash.turbine.test
import com.example.domain_core.utils.BaseResult
import com.example.domain_onboarding.useCases.SignInUseCase
import com.example.domain_onboarding.useCases.SignUpUseCase
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    val testDispatcher: TestDispatcher = StandardTestDispatcher(),
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private val username = "user"
    private val password = "password"
    private val passwordConfirm = "password"

    private lateinit var signInUseCase: SignInUseCase
    private lateinit var signUpUseCase: SignUpUseCase

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        signInUseCase = mockk()
        signUpUseCase = mockk()
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `successful sign up updates uiState to Success`() = runTest {
        coEvery {
            signUpUseCase.invoke(username, password, passwordConfirm)
        } returns BaseResult.Success(Unit)

        val authViewModel = AuthViewModel(signInUseCase, signUpUseCase, mainDispatcherRule.testDispatcher)

        authViewModel.uiState.test {
            assertTrue(awaitItem() is AuthUiState.None)

            authViewModel.signUp(username, password, password)
            assertTrue(awaitItem() is AuthUiState.Loading)

            assertTrue(awaitItem() is AuthUiState.Success)

            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) {
            signUpUseCase.invoke(any(), any(), any())
        }
    }

    @Test
    fun `failed sign up updates uiState to Error`() = runTest {
        coEvery {
            signUpUseCase.invoke(username, password, passwordConfirm)
        } returns BaseResult.Error(500, "Server error")

        val authViewModel = AuthViewModel(signInUseCase, signUpUseCase, mainDispatcherRule.testDispatcher)

        authViewModel.uiState.test {
            assertTrue(awaitItem() is AuthUiState.None)

            authViewModel.signUp(username, password, password)
            assertTrue(awaitItem() is AuthUiState.Loading)

            val latestState = awaitItem()
            assertTrue(latestState is AuthUiState.Error)
            latestState as AuthUiState.Error
            assertEquals(latestState.code, 500)
            assertEquals(latestState.message, "Server error")

            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) {
            signUpUseCase.invoke(any(), any(), any())
        }
    }

    @Test
    fun `failed with exception sign up updates uiState to Error`() = runTest {
        coEvery {
            signUpUseCase.invoke(username, password, passwordConfirm)
        } returns BaseResult.Exception("Runtime exception")

        val authViewModel = AuthViewModel(signInUseCase, signUpUseCase, mainDispatcherRule.testDispatcher)

        authViewModel.uiState.test {
            assertTrue(awaitItem() is AuthUiState.None)

            authViewModel.signUp(username, password, password)
            assertTrue(awaitItem() is AuthUiState.Loading)

            val latestState = awaitItem()
            assertTrue(latestState is AuthUiState.Error)
            latestState as AuthUiState.Error
            assertEquals(latestState.code, null)
            assertEquals(latestState.message, "Runtime exception")

            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) {
            signUpUseCase.invoke(any(), any(), any())
        }
    }

    @Test
    fun `successful sign in updates uiState to Success`() = runTest {
        coEvery {
            signInUseCase.invoke(username, password)
        } returns BaseResult.Success(Unit)

        val authViewModel = AuthViewModel(signInUseCase, signUpUseCase, mainDispatcherRule.testDispatcher)

        authViewModel.uiState.test {
            assertTrue(awaitItem() is AuthUiState.None)

            authViewModel.signIn(username, password)
            assertTrue(awaitItem() is AuthUiState.Loading)

            assertTrue(awaitItem() is AuthUiState.Success)

            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) {
            signInUseCase.invoke(any(), any())
        }
    }

    @Test
    fun `failed sign in updates uiState to Error`() = runTest {
        coEvery {
            signInUseCase.invoke(username, password)
        } returns BaseResult.Error(500, "Server error")

        val authViewModel = AuthViewModel(signInUseCase, signUpUseCase, mainDispatcherRule.testDispatcher)

        authViewModel.uiState.test {
            assertTrue(awaitItem() is AuthUiState.None)

            authViewModel.signIn(username, password)
            assertTrue(awaitItem() is AuthUiState.Loading)

            val latestState = awaitItem()
            assertTrue(latestState is AuthUiState.Error)
            latestState as AuthUiState.Error
            assertEquals(latestState.code, 500)
            assertEquals(latestState.message, "Server error")

            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) {
            signInUseCase.invoke(any(), any())
        }
    }

    @Test
    fun `failed with exception sign in updates uiState to Error`() = runTest {
        coEvery {
            signInUseCase.invoke(username, password)
        } returns BaseResult.Exception("Runtime exception")

        val authViewModel = AuthViewModel(signInUseCase, signUpUseCase, mainDispatcherRule.testDispatcher)

        authViewModel.uiState.test {
            assertTrue(awaitItem() is AuthUiState.None)

            authViewModel.signIn(username, password)
            assertTrue(awaitItem() is AuthUiState.Loading)

            val latestState = awaitItem()
            assertTrue(latestState is AuthUiState.Error)
            latestState as AuthUiState.Error
            assertEquals(latestState.code, null)
            assertEquals(latestState.message, "Runtime exception")

            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) {
            signInUseCase.invoke(any(), any())
        }
    }
}

