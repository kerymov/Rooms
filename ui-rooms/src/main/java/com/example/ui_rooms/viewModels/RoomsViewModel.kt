package com.example.ui_rooms.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain_core.utils.BaseResult
import com.example.domain_rooms.useCases.CreateRoomUseCase
import com.example.domain_rooms.useCases.DeleteRoomUseCase
import com.example.domain_rooms.useCases.GetRoomsUseCase
import com.example.domain_rooms.useCases.LoginRoomUseCase
import com.example.ui_rooms.mappers.mapToDomainModel
import com.example.ui_rooms.mappers.mapToUiModel
import com.example.ui_rooms.models.RoomDetailsUi
import com.example.ui_rooms.models.RoomUi
import com.example.ui_rooms.models.SettingsUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class Error(open val code: Int?, open val message: String?) {
    data class RoomsFetchingError(override val code: Int?, override val message: String?) : Error(code, message)
    data class OtherError(override val code: Int?, override val message: String?): Error(code, message)
}

enum class LoadingState {
    NONE,
    LOADING,
    REFRESHING
}

data class RoomsUiState(
    val rooms: List<RoomUi>? = null,
    val currentRoomDetails: RoomDetailsUi? = null,
    val isCreateRoomBottomSheetOpen: Boolean = false,
    val loadingState: LoadingState = LoadingState.NONE,
    val error: Error? = null
)

@HiltViewModel
class RoomsViewModel @Inject constructor(
    private val getRoomsUseCase: GetRoomsUseCase,
    private val createRoomUseCase: CreateRoomUseCase,
    private val loginRoomUseCase: LoginRoomUseCase,
    private val deleteRoomUseCase: DeleteRoomUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RoomsUiState())
    val uiState: StateFlow<RoomsUiState> = _uiState.asStateFlow()

    init {
        getRooms()
    }

    fun getRooms(isRefreshing: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { state ->
                state.copy(
                    loadingState = if (isRefreshing) LoadingState.REFRESHING else LoadingState.LOADING
                )
            }

            getRoomsUseCase.invoke()
                .catch { e ->
                    _uiState.update { state ->
                        state.copy(
                            loadingState = LoadingState.NONE,
                            error = Error.RoomsFetchingError(
                                code = null,
                                message = e.message
                            )
                        )
                    }
                }
                .collect { result ->
                    _uiState.update { state ->
                        when (result) {
                            is BaseResult.Success -> state.copy(
                                rooms = result.data.map { it.mapToUiModel() },
                            )

                            is BaseResult.Error -> state.copy(
                                error = Error.RoomsFetchingError(
                                    code = result.code,
                                    message = result.message
                                )
                            )

                            is BaseResult.Exception -> state.copy(
                                error = Error.OtherError(
                                    code = null,
                                    message = result.message
                                )
                            )
                        }
                    }
                }

            _uiState.update { state ->
                state.copy(loadingState = LoadingState.NONE)
            }
        }
    }

    fun createRoom(
        name: String,
        password: String? = null,
        settings: SettingsUi
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { state ->
                state.copy(loadingState = LoadingState.LOADING)
            }

            val roomDetailsResult =
                async { createRoomUseCase.invoke(name, password, settings.mapToDomainModel()) }

            _uiState.value = when (val result = roomDetailsResult.await()) {
                    is BaseResult.Success -> _uiState.value.copy(
                        currentRoomDetails = result.data.mapToUiModel()
                    )

                    is BaseResult.Error -> _uiState.value.copy(
                        error = Error.OtherError(
                            code = result.code,
                            message = result.message
                        )
                    )

                    is BaseResult.Exception -> _uiState.value.copy(
                        error = Error.OtherError(
                            code = null,
                            message = result.message
                        )
                    )
                }

            _uiState.update { state ->
                state.copy(loadingState = LoadingState.NONE)
            }
        }
    }

    fun loginRoom(name: String, password: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { state ->
                state.copy(
                    loadingState = LoadingState.LOADING,
                )
            }

            val roomDetailsResult = async { loginRoomUseCase.invoke(name, password) }

            _uiState.value = when (val result = roomDetailsResult.await()) {
                is BaseResult.Success -> _uiState.value.copy(
                    currentRoomDetails = result.data.mapToUiModel()
                )

                is BaseResult.Error -> _uiState.value.copy(
                    error = Error.OtherError(
                        code = result.code,
                        message = result.message
                    )
                )

                is BaseResult.Exception -> _uiState.value.copy(
                    error = Error.OtherError(
                        code = null,
                        message = result.message
                    )
                )
            }

            _uiState.update { state ->
                state.copy(loadingState = LoadingState.NONE)
            }
        }
    }

    fun deleteRoom(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { state ->
                state.copy(
                    loadingState = LoadingState.LOADING
                )
            }

            val deletionResult = async { deleteRoomUseCase.invoke(id) }

            _uiState.value = when (val result = deletionResult.await()) {
                is BaseResult.Success -> _uiState.value.copy(
                    rooms = _uiState.value.rooms?.filter { room -> room.id != id } ?: emptyList()
                )

                is BaseResult.Error -> _uiState.value.copy(
                    error = Error.OtherError(
                        code = result.code,
                        message = result.message
                    )
                )

                is BaseResult.Exception -> _uiState.value.copy(
                    error = Error.OtherError(
                        code = null,
                        message = result.message
                    )
                )
            }

            _uiState.update { state ->
                state.copy(loadingState = LoadingState.NONE)
            }
        }
    }

    fun toggleCreateRoomBottomSheet(isOpen: Boolean) {
        _uiState.update { state -> state.copy(isCreateRoomBottomSheetOpen = isOpen) }
    }
}