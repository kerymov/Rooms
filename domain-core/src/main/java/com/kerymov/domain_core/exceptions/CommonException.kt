package com.kerymov.domain_core.exceptions

sealed interface CommonException : AppException {
    data class ConnectionException(val cause: Throwable?) : CommonException
    data class BackendException(val statusCode: Int, val message: String?) : CommonException
    data object UnknownException : CommonException
}