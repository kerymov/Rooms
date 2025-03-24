package com.example.rooms.presentation.navigation

import androidx.compose.ui.graphics.vector.ImageVector

data class TopAppBar(
    val title: String,
    val navigationItem: InteractionItem? = null,
    val actions: List<InteractionItem> = emptyList()
)

data class InteractionItem(
    val icon: ImageVector,
    val onClick: () -> Unit
)

class TopAppBarBuilder {
    var title: String = ""
    var navigationItem: InteractionItem? = null
    var actions: List<InteractionItem> = emptyList()

    fun build(): TopAppBar = TopAppBar(title, navigationItem, actions)
}

fun topAppBar(block: TopAppBarBuilder.() -> Unit): TopAppBar = TopAppBarBuilder().apply(block).build()