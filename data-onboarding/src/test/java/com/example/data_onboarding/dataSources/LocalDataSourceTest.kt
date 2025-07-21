package com.example.data_onboarding.dataSources

import com.example.domain_core.preferences.Preferences
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class LocalDataSourceTest {

    private lateinit var preferences: Preferences
    private lateinit var localDataSource: LocalAccountDataSource

    @Before
    fun setUp() {
        preferences = mockk()
        localDataSource = LocalAccountDataSource(preferences)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `saveUser calls preferences saveUser`() = runBlocking {
        val username = "user"
        val authToken = "token"
        val expiresIn = 168

        coEvery { preferences.saveUser(any()) } returns Unit

        localDataSource.saveUser(username, authToken, expiresIn)

        coVerify(exactly = 1) { preferences.saveUser(any()) }
    }
}