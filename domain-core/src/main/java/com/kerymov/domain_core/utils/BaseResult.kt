package com.kerymov.domain_core.utils

import com.kerymov.domain_core.exceptions.AppException

sealed class BaseResult<out T : Any> {
    data class Success<T : Any>(val data: T) : BaseResult<T>()
    data class Error(val exception: AppException) : BaseResult<Nothing>()
}