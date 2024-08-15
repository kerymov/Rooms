package com.example.rooms.data.repository

import com.example.rooms.data.dataSource.RemoteAccountDataSource
import com.example.rooms.data.model.account.auth.UserSignInRequestDto
import com.example.rooms.data.model.account.auth.UserSignUpRequestDto
import com.example.rooms.data.network.NetworkResult
import com.example.rooms.domain.model.BaseResult
import com.example.rooms.domain.model.User
import com.example.rooms.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AccountRepositoryImpl(
    private val remoteDataSource: RemoteAccountDataSource
) : AccountRepository {
    override suspend fun signIn(username: String, password: String): Flow<BaseResult<User>> {
        val userSignInRequestDto = UserSignInRequestDto(username, password)
        val result = remoteDataSource.signIn(userSignInRequestDto)
        return flow {
            when (result) {
                is NetworkResult.Success -> {
                    val user = User(result.data.username)
                    emit(BaseResult.Success(user))
                }
                is NetworkResult.Error -> {
                    emit(BaseResult.Error(code = result.code, message = result.message))
                }
                is NetworkResult.Exception -> {
                    emit(BaseResult.Exception(message = result.e.message))
                }
            }
        }
    }

    override suspend fun signUp(username: String, password: String, passwordConfirm: String): Flow<BaseResult<User>> {
        val userSignUpRequestDto = UserSignUpRequestDto(username, password, passwordConfirm)
        val result = remoteDataSource.signUp(userSignUpRequestDto)
        return flow {
            when (result) {
                is NetworkResult.Success -> {
                    val user = User(result.data.username)
                    emit(BaseResult.Success(user))
                }
                is NetworkResult.Error -> {
                    emit(BaseResult.Error(code = result.code, message = result.message))
                }
                is NetworkResult.Exception -> {
                    emit(BaseResult.Exception(message = result.e.message))
                }
            }
        }
    }
}


//        var user: User? = null
//        var errorMessage: String? = null
//        runCatching {
//            val userSignInRequestDto = UserSignInRequestDto(username, password)
//            remoteDataSource.signIn(userSignInRequestDto)
//        }
//        .onSuccess { response ->
//            if (response.isSuccessful) {
//                val signInUsername = response.body()?.username
//                val signInError = response.body()?.errorMessage
//
//                user = signInUsername?.let { User(it) }
//                errorMessage = signInError
//            } else {
//                user = null
//                errorMessage = response.errorBody()?.string()
//            }
//        }
//        .onFailure { e ->
//            Log.e("TAG", "Exception during request -> ${e.localizedMessage}")
//        }
//
//        return user