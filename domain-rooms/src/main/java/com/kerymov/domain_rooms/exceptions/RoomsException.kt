package com.kerymov.domain_rooms.exceptions

import com.kerymov.domain_core.exceptions.AppException

sealed class RoomsException : AppException {
    data object RoomPasswordIsWrongException : RoomsException()
    data object InvalidRoomNameException : RoomsException()
    data object InvalidRoomPasswordException : RoomsException()
    data object RoomWithSameNameAlreadyExistsException : RoomsException()
}