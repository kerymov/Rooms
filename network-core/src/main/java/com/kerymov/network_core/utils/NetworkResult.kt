package com.kerymov.network_core.utils

import retrofit2.HttpException
import retrofit2.Response
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.security.cert.CertPathValidatorException
import java.util.concurrent.TimeoutException
import javax.net.ssl.SSLHandshakeException

sealed class NetworkResult<T : Any> {
    class Success<T : Any>(val data: T) : NetworkResult<T>()
    sealed class Error<T : Any>(val code: Int, val message: String?) : NetworkResult<T>() {
        data class ServerUnavailable<T : Any>(val response: Response<T>) : Error<T>(code = response.code(), message = response.message())
        data class BadRequest<T : Any>(val response: Response<T>) : Error<T>(code = response.code(), message = response.message())
        data class NotFound<T : Any>(val response: Response<T>) : Error<T>(code = response.code(), message = response.message())
        data class Unauthorized<T : Any>(val response: Response<T>) : Error<T>(code = response.code(), message = response.message())
        data class Forbidden<T : Any>(val response: Response<T>) : Error<T>(code = response.code(), message = response.message())
        data class TooManyRequests<T : Any>(val response: Response<T>) : Error<T>(code = response.code(), message = response.message())
        data class Unknown<T : Any>(val response: Response<T>) : Error<T>(code = response.code(), message = response.message())
    }
    sealed class Exception<T : Any>(val e: Throwable) : NetworkResult<T>() {
        data class NoInternet<T : Any>(val exception: kotlin.Exception) : Exception<T>(e = exception)
        data class Timeout<T : Any>(val exception: kotlin.Exception) : Exception<T>(e = exception)
        data class SSL<T : Any>(val exception: kotlin.Exception) : Exception<T>(e = exception)
        data class Unknown<T : Any>(val exception: kotlin.Exception) : Exception<T>(e = exception)
    }
}

suspend fun <T:Any> handleApi(
    execute: suspend() -> Response<T>
): NetworkResult<T> = try {
    val response = execute()
    val body = response.body()
    if (response.isSuccessful && body != null) {
        NetworkResult.Success(body)
    } else {
        when (response.code()) {
            400 -> NetworkResult.Error.BadRequest(response)
            401 -> NetworkResult.Error.Unauthorized(response)
            403 -> NetworkResult.Error.Forbidden(response)
            404 -> NetworkResult.Error.NotFound(response)
            429 -> NetworkResult.Error.TooManyRequests(response)
            in 500..599 -> NetworkResult.Error.ServerUnavailable(response)
            else -> NetworkResult.Error.Unknown(response)
        }
    }
} catch (e: UnknownHostException) {
    NetworkResult.Exception.NoInternet(e)
} catch (e: NoRouteToHostException) {
    NetworkResult.Exception.NoInternet(e)
} catch (e: SocketTimeoutException) {
    NetworkResult.Exception.Timeout(e)
} catch (e: TimeoutException) {
    NetworkResult.Exception.Timeout(e)
} catch (e: SSLHandshakeException) {
    NetworkResult.Exception.SSL(e)
} catch (e: CertPathValidatorException) {
    NetworkResult.Exception.SSL(e)
} catch (e: HttpException) {
    NetworkResult.Exception.Unknown(e)
}