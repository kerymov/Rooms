package com.kerymov.data_onboarding.repository

import com.kerymov.data_onboarding.dataSources.LocalAccountDataSource
import com.kerymov.data_onboarding.dataSources.RemoteAccountDataSource
import com.kerymov.data_onboarding.models.UserSignInRequest
import com.kerymov.data_onboarding.models.UserSignUpRequest
import com.kerymov.data_onboarding.mappers.UserMapper
import com.kerymov.domain_core.exceptions.CommonException
import com.kerymov.domain_core.utils.BaseResult
import com.kerymov.domain_core.utils.coroutines.IoDispatcher
import com.kerymov.domain_onboarding.exceptions.AuthException
import com.kerymov.domain_onboarding.models.User
import com.kerymov.domain_onboarding.repository.AccountRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AccountRepositoryImpl(
    private val remoteDataSource: RemoteAccountDataSource,
    private val localAccountDataSource: LocalAccountDataSource,
    private val mapper: UserMapper,
    @IoDispatcher private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : AccountRepository {

    override suspend fun signIn(username: String, password: String): BaseResult<Unit> {
        val userSignInRequest = UserSignInRequest(username, password)

        return withContext(dispatcher) {
            try {
                val response = remoteDataSource.signIn(userSignInRequest)
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    if (body.isSuccess) {
                        val user = mapper.mapToDomain(body)
                        saveUser(user)

                        BaseResult.Success(Unit)
                    } else {
                        when (body.errorMessage) {
                            "Invalid username", "Invalid password" -> BaseResult.Error(
                                exception = AuthException.InvalidCredentialsException
                            )

                            else -> BaseResult.Error(
                                exception = CommonException.UnknownException
                            )
                        }
                    }
                } else {
                    BaseResult.Error(
                        exception = CommonException.BackendException(
                            statusCode = response.code(),
                            message = response.message()
                        )
                    )
                }
            } catch (e: Exception) {
                BaseResult.Error(
                    exception = CommonException.ConnectionException(e)
                )
            }
        }
    }

    override suspend fun signUp(
        username: String,
        password: String,
        passwordConfirm: String
    ): BaseResult<Unit> {
        val userSignUpRequest = UserSignUpRequest(username, password, passwordConfirm)

        return withContext(dispatcher) {
            try {
                val response = remoteDataSource.signUp(userSignUpRequest)
                val body = response.body()

                if (response.isSuccessful && body != null) {
                    if (body.isSuccess) {
                        val user = mapper.mapToDomain(body)
                        saveUser(user)

                        BaseResult.Success(Unit)
                    } else {
                        when (body.errorMessage) {
                            "Provided username already registered" -> BaseResult.Error(
                                exception = AuthException.UserAlreadyExistsException
                            )
                            else -> BaseResult.Error(
                                exception = CommonException.UnknownException
                            )
                        }
                    }
                } else {
                    BaseResult.Error(
                        exception = CommonException.BackendException(
                            statusCode = response.code(),
                            message = response.message()
                        )
                    )
                }
            } catch (e: Exception) {
                BaseResult.Error(
                    exception = CommonException.ConnectionException(e)
                )
            }
        }
    }

    private suspend fun saveUser(user: User) {
        localAccountDataSource.saveUser(
            username = user.username,
            authToken = user.token,
            expiresIn = user.expiresIn
        )
    }
}
