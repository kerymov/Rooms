package com.example.rooms.data.network.room

import com.example.rooms.data.model.rooms.ResultDto
import com.example.rooms.data.model.rooms.SolveDto
import com.microsoft.signalr.HubConnectionBuilder
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class RoomService(authToken: String?) {

    private enum class Method(val raw: String) {
        FETCH_NEW_RESULT("NewResult"),
        SEND_SOLVE("SolveFinished"),
        FETCH_USER_LEFT("UserLeft"),
        FETCH_NEW_USER("NewUser"),
        SEND_ASK_NEW_SOLVE("AskForNewSolve"),
        SEND_FORCE_NEW_SOLVE("ForceNewSolve"),
        SEND_JOIN_ROOM("JoinGroup"),
        SEND_LEAVE_ROOM("LeaveGroup"),
        SEND_CHANGE_PUZZLE("ChangePuzzle"),
        FETCH_ROOM_REMOVED("Removed"),
        FETCH_RESULT_REMOVED("ResultRemove"),
    }

    val newUsers: Flow<String> = callbackFlow {
        connection.on(
            Method.FETCH_NEW_USER.raw,
            { username ->
                trySend(username)
            },
            String::class.java,
        )
        awaitClose {
            connection.stop()
        }
    }

    val leftUsers: Flow<String> = callbackFlow {
        connection.on(
            Method.FETCH_USER_LEFT.raw,
            { username -> trySend(username) },
            String::class.java,
        )
        awaitClose {
            connection.stop()
        }
    }

    val results: Flow<ResultDto> = callbackFlow {
        connection.on(
            Method.FETCH_NEW_RESULT.raw,
            { result -> trySend(result) },
            ResultDto::class.java,
        )
        awaitClose {
            connection.stop()
        }
    }

    val finishedSolves: Flow<SolveDto> = callbackFlow {
        connection.on(
            Method.SEND_SOLVE.raw,
            { solve -> trySend(solve) },
            SolveDto::class.java
        )
        awaitClose {
            connection.stop()
        }
    }

    fun joinRoom(roomName: String) {
        connection
            .start()
            .doOnComplete {
                connection.invoke(Method.SEND_JOIN_ROOM.raw, roomName)
            }
            .blockingAwait();
    }

    fun leaveRoom(roomName: String) {
        connection.invoke(Method.SEND_LEAVE_ROOM.raw, roomName)
            .doOnComplete {
                connection.stop()
            }
            .blockingAwait()
    }

    fun askForNewSolve(roomId: String) {
        connection.invoke(Method.SEND_ASK_NEW_SOLVE.raw, roomId)
    }

    fun forceNewSolve(roomId: String) {
        connection.invoke(Method.SEND_FORCE_NEW_SOLVE.raw, roomId)
    }

    fun changePuzzle(roomId: String) {
        TODO()
    }

    //    export interface NewUserResult {
//        roomId: string;
//        solveNumber: number;
//        timeInMilliseconds: number;
//        penalty: Penalty;
//    }
    fun sendSolve(solve: SolveDto) {
        connection.invoke(
            SolveDto::class.java,
            Method.SEND_SOLVE.raw,
            solve
        )
    }

    private val connection = HubConnectionBuilder
        .create("https://team-cubing.azurewebsites.net/api/hubs/room")
        .withAccessTokenProvider(
            Single.defer { Single.just(authToken ?: "") }
        )
        .build()
}