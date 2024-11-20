package com.example.rooms.domain.useCases.room

import com.example.rooms.domain.model.rooms.User
import com.example.rooms.domain.repository.RoomRepository
import kotlinx.coroutines.flow.Flow

class GetLeftUsersUseCase(
    private val repository: RoomRepository
) {

    fun invoke(): Flow<User> = repository.leftUsers
}