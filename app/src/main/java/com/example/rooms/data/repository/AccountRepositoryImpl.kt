package com.example.rooms.data.repository

import com.example.rooms.data.dataSource.account.RemoteAccountDataSource
import com.example.rooms.data.model.account.auth.UserDto
import com.example.rooms.data.model.account.auth.UserSignInRequestDto
import com.example.rooms.data.model.account.auth.UserSignUpRequestDto
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
        val userSignInRequestDto = UserSignInRequestDto(username, password)
        return flow {
            try {
                val response = remoteDataSource.signIn(userSignInRequestDto)
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    if (body.errorMessage == null) {
                        with(body) {
                            val user = User(username, token, expiresIn)
                            saveUser(user)
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
                            val user = User(username, token, expiresIn)
                            saveUser(user)
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
        AppSharedPreferences.userName = null
        AppSharedPreferences.authToken = null
        AppSharedPreferences.authTokenExpiresIn = null

//        dataStorePreferences.removeString(key = USER_KEY)
    }

    override suspend fun saveUser(user: User) {
//        val user = UserDto(user.username, user.token, user.expiresIn)
//
//        dataStorePreferences.putString(
//            key = USER_KEY,
//            value = user.toJson()
//        )

        AppSharedPreferences.userName = user.username
        AppSharedPreferences.authToken = user.token
        AppSharedPreferences.authTokenExpiresIn = user.expiresIn
    }

    override suspend fun getUser(): User? {
//        val userJson = dataStorePreferences.getString(key = USER_KEY)
//        val userDto = userJson?.let { UserDto.fromJson(it) }
//
//        return userDto?.toUser()
        val userName = AppSharedPreferences.userName
        val authToken = AppSharedPreferences.authToken
        val authTokenExpiresIn = AppSharedPreferences.authTokenExpiresIn

        if(userName == null || authToken == null || authTokenExpiresIn == null) return null
        return User(userName, authToken , authTokenExpiresIn)
    }

    private companion object {
        const val USER_KEY = "user"
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
