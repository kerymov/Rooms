package com.kerymov.rooms.presentation.navigation

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import com.kerymov.rooms.presentation.components.TopAppBarItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@Immutable
data class ScaffoldState(
    val topAppBar: TopAppBarItem? = null
)

class RootViewModel : ViewModel() {

    private val _scaffoldState = MutableStateFlow(ScaffoldState())
    val scaffoldState: StateFlow<ScaffoldState> = _scaffoldState.asStateFlow()

    fun updateTopAppBar(item: TopAppBarItem?) {
        _scaffoldState.update { state ->
            state.copy(topAppBar = item )
        }
    }
}