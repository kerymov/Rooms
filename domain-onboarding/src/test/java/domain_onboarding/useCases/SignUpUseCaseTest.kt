package domain_onboarding.useCases

import com.example.domain_core.utils.BaseResult
import com.example.domain_onboarding.repository.AccountRepository
import com.example.domain_onboarding.useCases.SignUpUseCase
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SignUpUseCaseTest {

    private lateinit var repository: AccountRepository
    private lateinit var useCase: SignUpUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = SignUpUseCase(repository)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `signUp returns Success when repository returns Success`() = runBlocking {
        val username = "user"
        val password = "pass"
        val passwordConfirm = "pass"

        coEvery { repository.signUp(any(), any(), any()) } returns BaseResult.Success(Unit)

        val result = useCase.invoke(username, password, passwordConfirm)

        assertTrue(result is BaseResult.Success)
        coVerify(exactly = 1) { repository.signUp(username, password, passwordConfirm) }
    }

    @Test
    fun `signUp returns Error when repository returns Error`() = runBlocking {
        val username = "user"
        val password = "pass"
        val passwordConfirm = "pass"

        coEvery { repository.signUp(any(), any(), any()) } returns BaseResult.Error(400, "Bad Request")

        val result = useCase.invoke(username, password, passwordConfirm)

        assertTrue(result is BaseResult.Error)
        result as BaseResult.Error
        assertEquals(result.code, 400)
        assertEquals(result.message, "Bad Request")
        coVerify(exactly = 1) { repository.signUp(username, password, passwordConfirm) }
    }

    @Test
    fun `signUp returns Exception when repository returns Exception`() = runBlocking {
        val username = "user"
        val password = "pass"
        val passwordConfirm = "pass"

        val errorMessage = "Network error"
        coEvery { repository.signUp(any(), any(), any()) } returns BaseResult.Exception(errorMessage)

        val result = useCase.invoke(username, password, passwordConfirm)

        assertTrue(result is BaseResult.Exception)
        result as BaseResult.Exception
        assertEquals(result.message, errorMessage)
        coVerify(exactly = 1) { repository.signUp(username, password, passwordConfirm) }
    }
}