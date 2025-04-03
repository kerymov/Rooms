package com.example.data_onboarding.repository

import com.example.data_onboarding.dataSources.LocalAccountDataSource
import com.example.data_onboarding.dataSources.RemoteAccountDataSource
import com.example.data_onboarding.models.UserSignInRequest
import com.example.data_onboarding.models.UserSignUpRequest
import com.example.data_onboarding.utils.UserMapper
import com.example.domain_core.utils.BaseResult
import com.example.domain_onboarding.models.User
import com.example.domain_onboarding.repository.AccountRepository
import retrofit2.HttpException
import javax.inject.Inject

class AccountRepositoryImpl(
    private val remoteDataSource: RemoteAccountDataSource,
    private val localAccountDataSource: LocalAccountDataSource,
    private val mapper: UserMapper
) : AccountRepository {

    override suspend fun signIn(username: String, password: String): BaseResult<Unit> {
        val userSignInRequest = UserSignInRequest(username, password)

        try {
            val response = remoteDataSource.signIn(userSignInRequest)
            val body = response.body()
            if (response.isSuccessful && body != null) {
                if (body.errorMessage == null) {
                    val user = mapper.mapToDomain(body)
                    saveUser(user)

                    return BaseResult.Success(Unit)
                } else {
                    return BaseResult.Error(code = body.statusCode, message = body.errorMessage)
                }
            } else {
                return BaseResult.Error(code = response.code(), message = response.message())
            }
        } catch (e: HttpException) {
            return BaseResult.Error(code = e.code(), message = e.message())
        } catch (e: Throwable) {
            return BaseResult.Exception(e.message)
        }
    }

    override suspend fun signUp(
        username: String,
        password: String,
        passwordConfirm: String
    ): BaseResult<Unit> {
        val userSignUpRequest = UserSignUpRequest(username, password, passwordConfirm)

        try {
            val response = remoteDataSource.signUp(userSignUpRequest)
            val body = response.body()

            if (response.isSuccessful && body != null) {
                if (body.errorMessage == null) {
                    val user = mapper.mapToDomain(body)
                    saveUser(user)

                    return BaseResult.Success(Unit)
                } else {
                    return BaseResult.Error(code = body.statusCode, message = body.errorMessage)
                }
            } else {
                return BaseResult.Error(code = response.code(), message = response.message())
            }
        } catch (e: HttpException) {
            return BaseResult.Error(code = e.code(), message = e.message())
        } catch (e: Throwable) {
            return BaseResult.Exception(e.message)
        }
    }

    private suspend fun saveUser(user: User) {
        localAccountDataSource.saveUser(
            username = user.username,
            authToken = user.token,
            expiresInt = user.expiresIn
        )
    }
}
