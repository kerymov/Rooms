package com.example.domain_room.useCases

import com.example.domain_room.models.User
import com.example.domain_room.repository.RoomRepository
import kotlinx.coroutines.flow.Flow

class GetNewUsersUseCase(
    private val repository: RoomRepository
) {

    fun invoke(): Flow<User> = repository.newUsers
}