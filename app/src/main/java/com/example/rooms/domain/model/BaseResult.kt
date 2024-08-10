package com.example.rooms.domain.model

sealed class BaseResult<out T : Any> {
    data class Success<T : Any>(val data: T) : BaseResult<T>()
    data class Error(val code: Int, val message: String?) : BaseResult<Nothing>()
    data class Exception(val message: String?) : BaseResult<Nothing>()
}
