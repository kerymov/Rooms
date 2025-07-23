package com.kerymov.data_onboarding.mappers

import com.kerymov.data_onboarding.models.UserAuthResponse
import com.kerymov.domain_onboarding.models.User
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