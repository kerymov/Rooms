package com.kerymov.domain_room.useCases

import com.kerymov.domain_room.models.User
import com.kerymov.domain_room.repository.RoomRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNewUsersUseCase @Inject constructor(
    private val repository: RoomRepository
) {

    fun invoke(): Flow<User> = repository.newUsers
}