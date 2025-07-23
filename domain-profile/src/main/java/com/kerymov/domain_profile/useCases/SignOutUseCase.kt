package com.kerymov.domain_profile.useCases

import com.kerymov.domain_profile.repository.UserRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke() = userRepository.signOut()
}