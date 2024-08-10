package com.example.rooms.data.network

import retrofit2.HttpException
import retrofit2.Response

sealed class NetworkResult<T : Any> {
    class Success<T : Any>(val data: T) : NetworkResult<T>()
    class Error<T : Any>(val code: Int, val message: String?) : NetworkResult<T>()
    class Exception<T : Any>(val e: Throwable) : NetworkResult<T>()
}

suspend fun <T:Any> handleApi(
    execute: suspend() -> Response<T>
): NetworkResult<T> = try {
    val response = execute()
    val body = response.body()
    if (response.isSuccessful && body != null) {
        NetworkResult.Success(body)
    } else {
        NetworkResult.Error(code = response.code(), message = response.message())
    }
} catch (e: HttpException) {
    NetworkResult.Error(code = e.code(), message = e.message())
} catch (e: Throwable) {
    NetworkResult.Exception(e)
}

//sealed interface ApiResult<T : Any>
//
//class ApiSuccess<T : Any>(val data: T) : NetworkResult<T>()
//sealed class ApiError<T : Any>(val code: Int, val message: String?) : NetworkResult<T>()
//class ApiException<T : Any>(val e: Throwable) : NetworkResult<T>()
//
//class BadRequest<T : Any>(val response: Response<T>) : ApiError<T>(response.code(), response.message())
//class Unauthorized<T : Any>(val response: Response<T>) : ApiError<T>(response.code(), response.message())
//class Unknown<T : Any>(val response: Response<T>) : ApiError<T>(response.code(), response.message())