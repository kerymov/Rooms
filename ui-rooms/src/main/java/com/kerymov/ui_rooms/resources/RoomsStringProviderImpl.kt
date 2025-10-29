package com.kerymov.ui_rooms.resources

import android.content.Context
import com.kerymov.domain_rooms.resources.RoomsStringProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomsStringProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : RoomsStringProvider {

    override val roomPasswordIsWrongErrorMessage: String
//        get() = context.getString(com.kerymov.ui_rooms.R.string.room_password_is_wrong_error_message)
        get() = "context.getString(com.kerymov.ui_rooms.R.string.room_password_is_wrong_error_message)"
    override val roomWithSameNameAlreadyExits: String
//        get() = context.getString(com.kerymov.ui_rooms.R.string.roomWithSameNameAlreadyExits)
        get() = "context.getString(com.kerymov.ui_rooms.R.string.roomWithSameNameAlreadyExits)"
}