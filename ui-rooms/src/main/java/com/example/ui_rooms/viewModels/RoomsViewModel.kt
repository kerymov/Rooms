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

sealed class RoomsUiState(
    open val rooms: List<RoomUi>? = null,
    open val currentRoomDetails: RoomDetailsUi? = null,
    open val isCreateRoomBottomSheetOpen: Boolean = false
) {

    data class Success(
        override val rooms: List<RoomUi> = emptyList(),
        override val currentRoomDetails: RoomDetailsUi? = null,
        override val isCreateRoomBottomSheetOpen: Boolean = false
    ) : RoomsUiState(
        rooms = rooms,
        currentRoomDetails = currentRoomDetails,
        isCreateRoomBottomSheetOpen = isCreateRoomBottomSheetOpen
    )

    sealed class Error(
        override val rooms: List<RoomUi>?,
        override val isCreateRoomBottomSheetOpen: Boolean = false,
        open val code: Int?,
        open val message: String?
    ) : RoomsUiState(rooms = rooms, currentRoomDetails = null, isCreateRoomBottomSheetOpen = isCreateRoomBottomSheetOpen) {
        data class RoomsFetchingError(
            override val isCreateRoomBottomSheetOpen: Boolean = false,
            override val code: Int?,
            override val message: String?
        ) : Error(
            rooms = null,
            isCreateRoomBottomSheetOpen = isCreateRoomBottomSheetOpen,
            code = code,
            message = message
        )

        data class OtherError(
            override val rooms: List<RoomUi>?,
            override val isCreateRoomBottomSheetOpen: Boolean = false,
            override val code: Int?,
            override val message: String?
        ) : Error(
            rooms = rooms,
            isCreateRoomBottomSheetOpen = isCreateRoomBottomSheetOpen,
            code = code,
            message = message
        )
    }
    data class Loading(override val rooms: List<RoomUi>?) : RoomsUiState(rooms = rooms, currentRoomDetails = null)
    data class Refreshing(override val rooms: List<RoomUi>?) : RoomsUiState(rooms = rooms, currentRoomDetails = null)
    data object None : RoomsUiState(rooms = null, currentRoomDetails = null)
}

@HiltViewModel
class RoomsViewModel @Inject constructor(
    private val getRoomsUseCase: GetRoomsUseCase,
    private val createRoomUseCase: CreateRoomUseCase,
    private val loginRoomUseCase: LoginRoomUseCase,
    private val deleteRoomUseCase: DeleteRoomUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<RoomsUiState>(RoomsUiState.None)
    val uiState: StateFlow<RoomsUiState> = _uiState.asStateFlow()

    init {
        getRooms()
    }

    fun getRooms(isRefreshing: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = if (isRefreshing) {
                RoomsUiState.Refreshing(_uiState.value.rooms)
            } else {
                RoomsUiState.Loading(_uiState.value.rooms)
            }

            getRoomsUseCase.invoke()
                .catch { e ->
                    _uiState.value = RoomsUiState.Error.RoomsFetchingError(
                        code = null,
                        message = e.message
                    )
                }
                .collect { result ->
                    _uiState.value = when (result) {
                        is BaseResult.Success -> RoomsUiState.Success(
                            rooms = result.data.map { it.mapToUiModel() },
                        )
                        is BaseResult.Error -> RoomsUiState.Error.RoomsFetchingError(
                            code = result.code,
                            message = result.message
                        )
                        is BaseResult.Exception -> RoomsUiState.Error.RoomsFetchingError(
                            code = null,
                            message = result.message
                        )
                    }
                }
        }
    }

    fun createRoom(
        name: String,
        password: String? = null,
        settings: SettingsUi
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = RoomsUiState.Loading(_uiState.value.rooms)

            val roomDetailsResult = async { createRoomUseCase.invoke(name, password, settings.mapToDomainModel()) }

            _uiState.value = when (val result = roomDetailsResult.await()) {
                is BaseResult.Success -> RoomsUiState.Success(
                    rooms = _uiState.value.rooms ?: emptyList(),
                    currentRoomDetails = result.data.mapToUiModel()
                )

                is BaseResult.Error -> RoomsUiState.Error.OtherError(
                    rooms = _uiState.value.rooms,
                    code = result.code,
                    message = result.message
                )

                is BaseResult.Exception -> RoomsUiState.Error.OtherError(
                    rooms = _uiState.value.rooms,
                    code = null,
                    message = result.message
                )
            }
        }
    }

    fun loginRoom(name: String, password: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = RoomsUiState.Loading(_uiState.value.rooms)

            val roomDetailsResult = async { loginRoomUseCase.invoke(name, password) }

            _uiState.value = when (val result = roomDetailsResult.await()) {
                is BaseResult.Success -> RoomsUiState.Success(
                    rooms = _uiState.value.rooms ?: emptyList(),
                    currentRoomDetails = result.data.mapToUiModel()
                )

                is BaseResult.Error -> RoomsUiState.Error.OtherError(
                    rooms = _uiState.value.rooms,
                    code = result.code,
                    message = result.message
                )

                is BaseResult.Exception -> RoomsUiState.Error.OtherError(
                    rooms = _uiState.value.rooms,
                    code = null,
                    message = result.message
                )
            }
        }
    }

    fun deleteRoom(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = RoomsUiState.Loading(_uiState.value.rooms)

            val deletionResult = async { deleteRoomUseCase.invoke(id) }

            _uiState.value = when (val result = deletionResult.await()) {
                is BaseResult.Success -> RoomsUiState.Success(
                    rooms = _uiState.value.rooms?.filter { room -> room.id != id } ?: emptyList(),
                    currentRoomDetails = null
                )

                is BaseResult.Error -> RoomsUiState.Error.OtherError(
                    rooms = _uiState.value.rooms,
                    code = result.code,
                    message = result.message
                )

                is BaseResult.Exception -> RoomsUiState.Error.OtherError(
                    rooms = _uiState.value.rooms,
                    code = null,
                    message = result.message
                )
            }
        }
    }

    fun toggleCreateRoomBottomSheet(isOpen: Boolean) {
        _uiState.update { state ->
            when(state) {
                is RoomsUiState.Success -> state.copy(isCreateRoomBottomSheetOpen = isOpen)
                is RoomsUiState.Error.OtherError -> state.copy(isCreateRoomBottomSheetOpen = isOpen)
                is RoomsUiState.Error.RoomsFetchingError -> state.copy(isCreateRoomBottomSheetOpen = isOpen)
                else -> state
            }
        }
    }

    fun checkIsUserAdmin(administratorUsername: String) {

    }
}