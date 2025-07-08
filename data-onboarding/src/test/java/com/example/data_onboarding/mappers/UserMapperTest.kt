package com.example.data_onboarding.mappers

import com.example.data_onboarding.models.UserAuthResponse
import com.example.domain_onboarding.models.User
import org.junit.Assert.assertEquals
import org.junit.Test

class UserMapperTest {

    private val userMapper: UserMapper = UserMapper()

    @Test
    fun `UserMapper should map UserAuthResponse to User`() {
        val userAuthResponse = UserAuthResponse(
            statusCode = 200,
            errorMessage = null,
            isSuccess = true,
            username = "user",
            token = "token",
            expiresIn = 168
        )

        val actual = userMapper.mapToDomain(userAuthResponse)
        val expected = User(
            username = "user",
            token = "token",
            expiresIn = 168,
        )
        assertEquals(expected, actual)
    }
}