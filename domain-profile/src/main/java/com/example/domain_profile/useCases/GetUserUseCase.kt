package com.example.domain_profile.useCases

import com.example.domain_core.model.User
import com.example.domain_core.utils.BaseResult
import com.example.domain_profile.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    operator fun invoke(): Flow<User?> {
        return userRepository.getUser()
    }
}