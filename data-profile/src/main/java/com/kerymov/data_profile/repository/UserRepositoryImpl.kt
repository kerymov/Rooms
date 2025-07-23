package com.kerymov.data_profile.repository

import com.kerymov.data_profile.dataSources.LocalUserDataSource
import com.kerymov.data_profile.dataSources.RemoteUserDataSource
import com.kerymov.domain_core.model.User
import com.kerymov.domain_profile.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(
    private val localUserDataSource: LocalUserDataSource,
    private val remoteUserDataSource: RemoteUserDataSource
) : UserRepository {
    override fun getUser(): Flow<User?> = localUserDataSource.getUser()

    override suspend fun signOut() = localUserDataSource.signOut()
}