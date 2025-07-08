package com.example.data_onboarding.dataSources

import com.example.data_onboarding.models.UserAuthResponse
import com.example.data_onboarding.models.UserSignInRequest
import com.example.data_onboarding.service.AccountApi
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class RemoteDataSourceTest {

    private lateinit var accountApi: AccountApi
    private lateinit var remoteAccountDataSource: RemoteAccountDataSource

    @Before
    fun setUp() {
        accountApi = mockk()
        remoteAccountDataSource = RemoteAccountDataSource(accountApi)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `signIn returns response from api`() = runBlocking {
        val request = UserSignInRequest("user", "password")
        val expectedResponse = UserAuthResponse(
            errorMessage = null,
            expiresIn = 168,
            isSuccess = true,
            statusCode = 200,
            token = "token",
            username = "user"
        )
        coEvery { accountApi.signIn(request) } returns Response.success(expectedResponse)

        val response = remoteAccountDataSource.signIn(request)

        assertTrue(response.isSuccessful)
        assertEquals(expectedResponse, response.body())
    }
}