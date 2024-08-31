package com.example.rooms.data.repository

import com.example.rooms.data.dataSource.LocalAccountDataSource
import com.example.rooms.data.dataSource.RemoteAccountDataSource
import com.example.rooms.data.model.account.auth.UserDto
import com.example.rooms.data.model.account.auth.UserSignInRequestDto
import com.example.rooms.data.model.account.auth.UserSignUpRequestDto
import com.example.rooms.data.network.NetworkResult
import com.example.rooms.domain.model.BaseResult
import com.example.rooms.domain.model.User
import com.example.rooms.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException

class AccountRepositoryImpl(
    private val localDataSource: LocalAccountDataSource,
    private val remoteDataSource: RemoteAccountDataSource
) : AccountRepository {
    override suspend fun signIn(username: String, password: String): Flow<BaseResult<User>> {
        val userSignInRequestDto = UserSignInRequestDto(username, password)
        return flow {
            try {
                val response = remoteDataSource.signIn(userSignInRequestDto)
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    if (body.errorMessage == null) {
                        with(body) {
                            saveUser(username, token, expiresIn)
                        }

                        val user = with(body) {
                            User(
                                username = username,
                                token = token,
                                expiresIn = expiresIn,
                            )
                        }

                        emit(BaseResult.Success(user))
                    } else {
                        emit(BaseResult.Error(code = body.statusCode, message = body.errorMessage))
                    }
                } else {
                    emit(BaseResult.Error(code = response.code(), message = response.message()))
                }
            } catch (e: HttpException) {
                emit(BaseResult.Error(code = e.code(), message = e.message()))
            }
            catch (e: Throwable) {
                emit(BaseResult.Exception(e.message))
            }
        }
    }

    override suspend fun signUp(
        username: String,
        password: String,
        passwordConfirm: String
    ): Flow<BaseResult<User>> {
        val userSignUpRequestDto = UserSignUpRequestDto(username, password, passwordConfirm)
        return flow {
            try {
                val response = remoteDataSource.signUp(userSignUpRequestDto)
                val body = response.body()

                if (response.isSuccessful && body != null) {
                    if (body.errorMessage == null) {
                        with(body) {
                            saveUser(username, token, expiresIn)
                        }

                        val user = with(body) {
                            User(
                                username = username,
                                token = token,
                                expiresIn = expiresIn,
                            )
                        }

                        emit(BaseResult.Success(user))
                    } else {
                        emit(BaseResult.Error(code = body.statusCode, message = body.errorMessage))
                    }
                } else {
                    emit(BaseResult.Error(code = response.code(), message = response.message()))
                }
            } catch (e: HttpException) {
                emit(BaseResult.Error(code = e.code(), message = e.message()))
            }
            catch (e: Throwable) {
                emit(BaseResult.Exception(e.message))
            }
        }
    }

    override suspend fun signOut() {
        localDataSource.removeUser()
    }

    override suspend fun saveUser(username: String, token: String, expiresIn: Int) {
        val user = UserDto(username, token, expiresIn)
        localDataSource.saveUser(user)
    }

    override suspend fun getUser(): Flow<BaseResult<User>> {
        return localDataSource.getUser().map { userDto ->
            userDto?.let {
                BaseResult.Success(it.toUser())
            } ?: BaseResult.Error(null, "User not found")
        }
    }
}

private fun UserDto.toUser(): User {
    return with(this) {
        User(
            username = username,
            token =  token,
            expiresIn = expiresIn,
        )
    }
}
