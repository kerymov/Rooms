package com.kerymov.domain_rooms.resources

import com.example.essentials_core.resources.StringProvider

interface RoomsStringProvider : StringProvider {

    val roomPasswordIsWrongErrorMessage: String
    val roomWithSameNameAlreadyExits: String
}