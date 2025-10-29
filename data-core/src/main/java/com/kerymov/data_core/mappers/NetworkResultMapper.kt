package com.kerymov.data_core.mappers

import com.kerymov.domain_core.exceptions.CommonException
import com.kerymov.domain_core.utils.BaseResult
import com.kerymov.network_core.AlwaysSuccessfulResponse
import com.kerymov.network_core.utils.NetworkResult

class NetworkResultMapper {

    fun <T : Any, R : Any> mapToBaseResult(
        networkResult: NetworkResult<T>,
        transform: (T) -> R
    ): BaseResult<R> {
        return when (networkResult) {
            is NetworkResult.Success -> BaseResult.Success(transform(networkResult.data))
            is NetworkResult.Error -> BaseResult.Error(
                CommonException.BackendException(
                    statusCode = networkResult.code,
                    message = networkResult.message
                )
            )
            is NetworkResult.Exception -> BaseResult.Error(
                CommonException.ConnectionException(cause = networkResult.e)
            )
        }
    }

    fun <T : AlwaysSuccessfulResponse, R : Any> mapAlwaysSuccessfulResponseToBaseResult(
        networkResult: NetworkResult<T>,
        transform: (T) -> R,
        handleDomainError: (NetworkResult.Success<T>) -> BaseResult.Error
    ): BaseResult<R> {
        return when (networkResult) {
            is NetworkResult.Success -> {
                if (networkResult.data.isSuccess) {
                    BaseResult.Success(transform(networkResult.data))
                } else {
                    handleDomainError(networkResult)
                }
            }

            is NetworkResult.Error -> BaseResult.Error(
                CommonException.BackendException(
                    statusCode = networkResult.code,
                    message = networkResult.message
                )
            )
            is NetworkResult.Exception -> BaseResult.Error(
                CommonException.ConnectionException(cause = networkResult.e)
            )
        }
    }
}
