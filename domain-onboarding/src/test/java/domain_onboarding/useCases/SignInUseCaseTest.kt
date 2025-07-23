package domain_onboarding.useCases

import com.kerymov.domain_core.utils.BaseResult
import com.kerymov.domain_onboarding.repository.AccountRepository
import com.kerymov.domain_onboarding.useCases.SignInUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.clearAllMocks
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SignInUseCaseTest {
    private lateinit var repository: AccountRepository
    private lateinit var useCase: SignInUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = SignInUseCase(repository)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `signIn returns Success when repository returns Success`() = runBlocking {
        val username = "user"
        val password = "pass"

        coEvery { repository.signIn(any(), any()) } returns BaseResult.Success(Unit)

        val result = useCase.invoke(username, password)

        assertTrue(result is BaseResult.Success)
        coVerify(exactly = 1) { repository.signIn(username, password) }
    }

    @Test
    fun `signIn returns Error when repository returns Error`() = runBlocking {
        val username = "user"
        val password = "pass"

        coEvery { repository.signIn(any(), any()) } returns BaseResult.Error(401, "Unauthorized")

        val result = useCase.invoke(username, password)

        assertTrue(result is BaseResult.Error)
        result as BaseResult.Error
        assertEquals(401, result.code)
        assertEquals("Unauthorized", result.message)
        coVerify(exactly = 1) { repository.signIn(username, password) }
    }

    @Test
    fun `signIn returns Exception when repository returns Exception`() = runBlocking {
        val username = "user"
        val password = "pass"

        coEvery { repository.signIn(any(), any()) } returns BaseResult.Exception("Network error")

        val result = useCase.invoke(username, password)

        assertTrue(result is BaseResult.Exception)
        result as BaseResult.Exception
        assertEquals("Network error", result.message)
        coVerify(exactly = 1) { repository.signIn(username, password) }
    }
}