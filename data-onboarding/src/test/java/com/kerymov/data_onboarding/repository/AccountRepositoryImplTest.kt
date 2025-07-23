package com.kerymov.data_onboarding.repository

import com.kerymov.data_onboarding.dataSources.LocalAccountDataSource
import com.kerymov.data_onboarding.dataSources.RemoteAccountDataSource
import com.kerymov.data_onboarding.mappers.UserMapper
import com.kerymov.data_onboarding.models.UserAuthResponse
import com.kerymov.data_onboarding.models.UserSignInRequest
import com.kerymov.data_onboarding.models.UserSignUpRequest
import com.kerymov.domain_core.utils.BaseResult
import com.kerymov.domain_onboarding.models.User
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class AccountRepositoryImplTest {

    private lateinit var remoteAccountDataSource: RemoteAccountDataSource
    private lateinit var localAccountDataSource: LocalAccountDataSource
    private lateinit var userMapper: UserMapper
    private lateinit var accountRepository: AccountRepositoryImpl

    @Before
    fun setUp() {
        remoteAccountDataSource = mockk()
        localAccountDataSource = mockk()
        userMapper = mockk()
        accountRepository = AccountRepositoryImpl(remoteAccountDataSource, localAccountDataSource, userMapper)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `signIn returns Success when response in successful, body and errorMessage are not null`() = runBlocking {
        val username = "user"
        val password = "password"
        val token = "token"
        val expiresIn = 168

        val userAuthResponse = UserAuthResponse(
            statusCode = 200,
            isSuccess = true,
            errorMessage = null,
            expiresIn = expiresIn,
            username = username,
            token = token
        )
        coEvery { remoteAccountDataSource.signIn(any()) } returns Response.success(userAuthResponse)
        coEvery { userMapper.mapToDomain(any()) } returns User(username, token, expiresIn = expiresIn)
        coEvery { localAccountDataSource.saveUser(any(), any(), any()) } returns Unit

        val result = accountRepository.signIn(username, password)

        assertTrue(result is BaseResult.Success)
        coVerify(exactly = 1) { remoteAccountDataSource.signIn(UserSignInRequest(username, password)) }
        coVerify(exactly = 1) { localAccountDataSource.saveUser(username, token, expiresIn = expiresIn) }
        coVerify(exactly = 1) { userMapper.mapToDomain(userAuthResponse) }
    }

    @Test
    fun `signIn returns Success when response in successful, body is null but error message is not null`() = runBlocking {
        val username = "user"
        val password = "password"
        val errorMessage = "Invalid credentials"

        val userAuthResponse = UserAuthResponse(
            statusCode = 0,
            isSuccess = false,
            errorMessage = errorMessage,
            expiresIn = 0,
            username = null,
            token = null
        )
        coEvery { remoteAccountDataSource.signIn(any()) } returns Response.success(userAuthResponse)

        val result = accountRepository.signIn(username, password)

        assertTrue(result is BaseResult.Error)
        result as BaseResult.Error
        assertEquals(result.code, 0)
        assertEquals(result.message, errorMessage)

        coVerify(exactly = 1) { remoteAccountDataSource.signIn(UserSignInRequest(username, password)) }
        coVerify(exactly = 0) { localAccountDataSource.saveUser(any(), any(), any()) }
        coVerify(exactly = 0) { userMapper.mapToDomain(any()) }
    }

    @Test
    fun `signIn returns Success when response in successful but body is null`() = runBlocking {
        val username = "user"
        val password = "password"
        val errorResponse = Response.success<UserAuthResponse>(null)

        coEvery { remoteAccountDataSource.signIn(any()) } returns errorResponse

        val result = accountRepository.signIn(username, password)

        assertTrue(result is BaseResult.Error)
        result as BaseResult.Error
        assertEquals(result.code, errorResponse.code())
        assertEquals(result.message, errorResponse.message())

        coVerify(exactly = 1) { remoteAccountDataSource.signIn(UserSignInRequest(username, password)) }
        coVerify(exactly = 0) { localAccountDataSource.saveUser(any(), any(), any()) }
        coVerify(exactly = 0) { userMapper.mapToDomain(any()) }
    }

    @Test
    fun `signIn returns Error when response in failed`() = runBlocking {
        val username = "user"
        val password = "password"
        val response = Response.error<UserAuthResponse>(
            400,
            "Bad Request".toResponseBody(null)
        )

        coEvery { remoteAccountDataSource.signIn(any()) } returns response

        val result = accountRepository.signIn(username, password)

        assertTrue(result is BaseResult.Error)
        result as BaseResult.Error
        assertEquals(result.code, response.code())
        assertEquals(result.message, response.message())

        coVerify(exactly = 1) { remoteAccountDataSource.signIn(UserSignInRequest(username, password)) }
        coVerify(exactly = 0) { localAccountDataSource.saveUser(any(), any(), any()) }
        coVerify(exactly = 0) { userMapper.mapToDomain(any()) }
    }

    @Test
    fun `signIn returns Exception if exception was thrown`() = runBlocking {
        val username = "user"
        val password = "password"
        val exception = RuntimeException("Network error")

        coEvery { remoteAccountDataSource.signIn(any()) } throws exception

        val result = accountRepository.signIn(username, password)

        assertTrue(result is BaseResult.Exception)
        result as BaseResult.Exception
        assertEquals(result.message, exception.message)

        coVerify(exactly = 1) { remoteAccountDataSource.signIn(UserSignInRequest(username, password)) }
        coVerify(exactly = 0) { localAccountDataSource.saveUser(any(), any(), any()) }
        coVerify(exactly = 0) { userMapper.mapToDomain(any()) }
    }

    @Test
    fun `signUp returns Success when response is successful and body has no errorMessage`() = runBlocking {
        val username = "user"
        val password = "password"
        val passwordConfirm = "password"
        val token = "token"
        val expiresIn = 168

        val userAuthResponse = UserAuthResponse(
            statusCode = 200,
            isSuccess = true,
            errorMessage = null,
            expiresIn = expiresIn,
            username = username,
            token = token
        )
        val user = User(username, token, expiresIn)

        coEvery { remoteAccountDataSource.signUp(any()) } returns Response.success(userAuthResponse)
        coEvery { userMapper.mapToDomain(any()) } returns user
        coEvery { localAccountDataSource.saveUser(any(), any(), any()) } returns Unit

        val result = accountRepository.signUp(username, password, passwordConfirm)

        assertTrue(result is BaseResult.Success)
        coVerify(exactly = 1) { remoteAccountDataSource.signUp(UserSignUpRequest(username, password, passwordConfirm)) }
        coVerify(exactly = 1) { userMapper.mapToDomain(userAuthResponse) }
        coVerify(exactly = 1) { localAccountDataSource.saveUser(username, token, expiresIn) }
    }

    @Test
    fun `signUp returns Error when response is successful and body has errorMessage`() = runBlocking {
        val username = "user"
        val password = "password"
        val passwordConfirm = "password"
        val errorMessage = "User already exists"
        val userAuthResponse = UserAuthResponse(
            statusCode = 409,
            isSuccess = false,
            errorMessage = errorMessage,
            expiresIn = 0,
            username = null,
            token = null
        )

        coEvery { remoteAccountDataSource.signUp(any()) } returns Response.success(userAuthResponse)

        val result = accountRepository.signUp(username, password, passwordConfirm)

        assertTrue(result is BaseResult.Error)
        result as BaseResult.Error
        assertEquals(409, result.code)
        assertEquals(errorMessage, result.message)
        coVerify(exactly = 1) { remoteAccountDataSource.signUp(UserSignUpRequest(username, password, passwordConfirm)) }
        coVerify(exactly = 0) { userMapper.mapToDomain(any()) }
        coVerify(exactly = 0) { localAccountDataSource.saveUser(any(), any(), any()) }
    }

    @Test
    fun `signUp returns Error when response is successful but body is null`() = runBlocking {
        val username = "user"
        val password = "password"
        val passwordConfirm = "password"
        val response = Response.success<UserAuthResponse>(null)
        coEvery { remoteAccountDataSource.signUp(any()) } returns response

        val result = accountRepository.signUp(username, password, passwordConfirm)

        assertTrue(result is BaseResult.Error)
        result as BaseResult.Error
        assertEquals(response.code(), result.code)
        assertEquals(response.message(), result.message)
        coVerify(exactly = 1) { remoteAccountDataSource.signUp(UserSignUpRequest(username, password, passwordConfirm)) }
        coVerify(exactly = 0) { userMapper.mapToDomain(any()) }
        coVerify(exactly = 0) { localAccountDataSource.saveUser(any(), any(), any()) }
    }

    @Test
    fun `signUp returns Error when response is not successful`() = runBlocking {
        val username = "user"
        val password = "password"
        val passwordConfirm = "password"
        val response = Response.error<UserAuthResponse>(
            400,
            "Bad Request".toResponseBody(null)
        )
        coEvery { remoteAccountDataSource.signUp(any()) } returns response

        val result = accountRepository.signUp(username, password, passwordConfirm)

        assertTrue(result is BaseResult.Error)
        result as BaseResult.Error
        assertEquals(response.code(), result.code)
        assertEquals(response.message(), result.message)
        coVerify(exactly = 1) { remoteAccountDataSource.signUp(UserSignUpRequest(username, password, passwordConfirm)) }
        coVerify(exactly = 0) { userMapper.mapToDomain(any()) }
        coVerify(exactly = 0) { localAccountDataSource.saveUser(any(), any(), any()) }
    }

    @Test
    fun `signUp returns Exception when remoteDataSource throws exception`() = runBlocking {
        val username = "user"
        val password = "password"
        val passwordConfirm = "password"
        val exception = RuntimeException("Network error")
        coEvery { remoteAccountDataSource.signUp(any()) } throws exception

        val result = accountRepository.signUp(username, password, passwordConfirm)

        assertTrue(result is BaseResult.Exception)
        result as BaseResult.Exception
        assertEquals(exception.message, result.message)
        coVerify(exactly = 1) { remoteAccountDataSource.signUp(UserSignUpRequest(username, password, passwordConfirm)) }
        coVerify(exactly = 0) { userMapper.mapToDomain(any()) }
        coVerify(exactly = 0) { localAccountDataSource.saveUser(any(), any(), any()) }
    }
}