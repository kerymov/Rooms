package com.kerymov.domain_onboarding.exceptions

import com.kerymov.domain_core.exceptions.AppException

sealed class AuthException : AppException {
    data object InvalidCredentialsException : AuthException()
    data object UserAlreadyExistsException : AuthException()
}