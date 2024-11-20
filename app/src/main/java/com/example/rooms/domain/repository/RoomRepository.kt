package com.example.rooms.domain.repository

import com.example.rooms.domain.model.BaseResult
import com.example.rooms.domain.model.rooms.Scramble
import com.example.rooms.domain.model.rooms.User
import kotlinx.coroutines.flow.Flow

interface RoomRepository {

    fun joinRoom(roomName: String)

    fun leaveRoom(roomName: String)

    val newUsers: Flow<User>

    suspend fun getScramble(puzzle: Int): BaseResult<Scramble>
}