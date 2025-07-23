package com.kerymov.domain_profile.useCases

import com.kerymov.domain_core.model.User
import com.kerymov.domain_profile.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    operator fun invoke(): Flow<User?> {
        return userRepository.getUser()
    }
}