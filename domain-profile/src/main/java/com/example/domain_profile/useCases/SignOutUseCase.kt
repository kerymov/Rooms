package com.example.domain_profile.useCases

import com.example.domain_core.utils.BaseResult
import com.example.domain_profile.repository.UserRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke() = userRepository.signOut()
}