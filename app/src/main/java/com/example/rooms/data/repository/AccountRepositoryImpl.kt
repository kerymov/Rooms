package com.example.rooms.data.repository

import com.example.rooms.data.dataSource.account.RemoteAccountDataSource
import com.example.rooms.data.model.account.auth.mappers.mapToDomainModel
import com.example.rooms.data.model.account.auth.network.UserSignInRequest
import com.example.rooms.data.model.account.auth.network.UserSignUpRequest
import com.example.rooms.data.utils.AppSharedPreferences
import com.example.rooms.domain.model.BaseResult
import com.example.rooms.domain.model.User
import com.example.rooms.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class AccountRepositoryImpl(
    private val remoteDataSource: RemoteAccountDataSource
) : AccountRepository {

    override suspend fun signIn(username: String, password: String): Flow<BaseResult<User>> {
        val userSignInRequest = UserSignInRequest(username, password)

        return flow {
            try {
                val response = remoteDataSource.signIn(userSignInRequest)
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    if (body.errorMessage == null) {
                        val user = body.mapToDomainModel()

                        saveUser(user)
                        emit(BaseResult.Success(user))
                    } else {
                        emit(BaseResult.Error(code = body.statusCode, message = body.errorMessage))
                    }
                } else {
                    emit(BaseResult.Error(code = response.code(), message = response.message()))
                }
            } catch (e: HttpException) {
                emit(BaseResult.Error(code = e.code(), message = e.message()))
            } catch (e: Throwable) {
                emit(BaseResult.Exception(e.message))
            }
        }
    }

    override suspend fun signUp(
        username: String,
        password: String,
        passwordConfirm: String
    ): Flow<BaseResult<User>> {
        val userSignUpRequest = UserSignUpRequest(username, password, passwordConfirm)
        return flow {
            try {
                val response = remoteDataSource.signUp(userSignUpRequest)
                val body = response.body()

                if (response.isSuccessful && body != null) {
                    if (body.errorMessage == null) {
                        val user = body.mapToDomainModel()

                        saveUser(user)
                        emit(BaseResult.Success(user))
                    } else {
                        emit(BaseResult.Error(code = body.statusCode, message = body.errorMessage))
                    }
                } else {
                    emit(BaseResult.Error(code = response.code(), message = response.message()))
                }
            } catch (e: HttpException) {
                emit(BaseResult.Error(code = e.code(), message = e.message()))
            } catch (e: Throwable) {
                emit(BaseResult.Exception(e.message))
            }
        }
    }

    override suspend fun signOut() {
        AppSharedPreferences.userName = null
        AppSharedPreferences.authToken = null
        AppSharedPreferences.authTokenExpiresIn = null
    }

    override suspend fun saveUser(user: User) {
        AppSharedPreferences.userName = user.username
        AppSharedPreferences.authToken = user.token
        AppSharedPreferences.authTokenExpiresIn = user.expiresIn
    }

    override suspend fun getUser(): User? {
        val userName = AppSharedPreferences.userName
        val authToken = AppSharedPreferences.authToken
        val authTokenExpiresIn = AppSharedPreferences.authTokenExpiresIn

        if (userName == null || authToken == null || authTokenExpiresIn == null) return null
        return User(userName, authToken, authTokenExpiresIn)
    }
}
