package com.kerymov.data_onboarding.repository

import com.kerymov.data_onboarding.dataSources.LocalAccountDataSource
import com.kerymov.data_onboarding.dataSources.RemoteAccountDataSource
import com.kerymov.data_onboarding.models.UserSignInRequest
import com.kerymov.data_onboarding.models.UserSignUpRequest
import com.kerymov.data_onboarding.mappers.UserMapper
import com.kerymov.domain_core.utils.BaseResult
import com.kerymov.domain_core.utils.coroutines.IoDispatcher
import com.kerymov.domain_onboarding.models.User
import com.kerymov.domain_onboarding.repository.AccountRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

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
                    if (body.errorMessage == null) {
                        val user = mapper.mapToDomain(body)
                        saveUser(user)

                        BaseResult.Success(Unit)
                    } else {
                        BaseResult.Error(code = body.statusCode, message = body.errorMessage)
                    }
                } else {
                    BaseResult.Error(code = response.code(), message = response.message())
                }
            } catch (e: HttpException) {
                BaseResult.Error(code = e.code(), message = e.message())
            } catch (e: Throwable) {
                BaseResult.Exception(e.message)
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
                    if (body.errorMessage == null) {
                        val user = mapper.mapToDomain(body)
                        saveUser(user)

                        BaseResult.Success(Unit)
                    } else {
                        BaseResult.Error(code = body.statusCode, message = body.errorMessage)
                    }
                } else {
                    BaseResult.Error(code = response.code(), message = response.message())
                }
            } catch (e: HttpException) {
                BaseResult.Error(code = e.code(), message = e.message())
            } catch (e: Throwable) {
                BaseResult.Exception(e.message)
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
