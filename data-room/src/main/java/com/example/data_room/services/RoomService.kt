package com.example.data_room.services

import com.example.data_common_speedcubing.models.ResultDto
import com.example.data_common_speedcubing.models.SolveDto
import com.example.data_room.models.NewSolveResultDto
import com.microsoft.signalr.HubConnectionBuilder
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class RoomService {

    private enum class Method(val raw: String) {
        FETCH_AND_SEND_NEW_RESULT("NewResult"),
        FETCH_FINISHED_SOLVE("SolveFinished"),
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
            Method.FETCH_AND_SEND_NEW_RESULT.raw,
            { result -> trySend(result) },
            ResultDto::class.java,
        )
        awaitClose {
            connection.stop()
        }
    }

    val finishedSolves: Flow<SolveDto> = callbackFlow {
        connection.on(
            Method.FETCH_FINISHED_SOLVE.raw,
            { solve ->
                trySend(solve)
            },
            SolveDto::class.java
        )
        awaitClose {
            connection.stop()
        }
    }

    fun joinRoom(roomName: String, onComplete: () -> Unit) {
        connection
            .start()
            .doOnComplete {
                connection.invoke(Method.SEND_JOIN_ROOM.raw, roomName)
                onComplete()
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

    fun sendSolveResult(result: NewSolveResultDto) {
        connection
            .invoke(
                Method.FETCH_AND_SEND_NEW_RESULT.raw,
                result
            )
    }

    private val connection = HubConnectionBuilder
        .create("https://team-cubing.azurewebsites.net/api/hubs/room")
        .withHeaders(mapOf("Content-Type" to "application/json"))
        .build()
}
//.withAccessTokenProvider(
//    Single.defer { Single.just(authToken ?: "") }
//)