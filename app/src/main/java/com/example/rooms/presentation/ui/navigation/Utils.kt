package com.example.rooms.presentation.ui.navigation

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder

/**
 * [NavController.navigate] is an extension for [NavController]
 * to navigate between [NavSection]'s and [NavSection.Screen]'s
 * with [Parcelable] params or params in [Bundle]
 */

fun NavController.navigate(
    section: NavSection,
    param: Pair<String, Parcelable>? = null,
    builder: NavOptionsBuilder.() -> Unit = { }
) {
    param?.let { this.currentBackStackEntry?.arguments?.putParcelable(param.first, param.second) }

    navigate(section.name, builder)
}

fun NavController.navigate(
    section: NavSection,
    params: List<Pair<String, Parcelable>>? = null,
    builder: NavOptionsBuilder.() -> Unit = { }
) {
    params?.let {
        val arguments = this.currentBackStackEntry?.arguments
        params.forEach { arguments?.putParcelable(it.first, it.second) }
    }

    navigate(section.name, builder)
}

fun NavController.navigate(
    section: NavSection,
    params: Bundle? = null,
    builder: NavOptionsBuilder.() -> Unit = { }
) {
    this.currentBackStackEntry?.arguments?.putAll(params)

    navigate(section.name, builder)
}

fun NavController.navigate(
    screen: NavSection.Screen,
    param: Pair<String, Parcelable>? = null,
    builder: NavOptionsBuilder.() -> Unit = { }
) {
    param?.let { this.currentBackStackEntry?.arguments?.putParcelable(param.first, param.second) }

    navigate(screen.name, builder)
}

fun NavController.navigate(
    screen: NavSection.Screen,
    params: List<Pair<String, Parcelable>>? = null,
    builder: NavOptionsBuilder.() -> Unit = { }
) {
    params?.let {
        val arguments = this.currentBackStackEntry?.arguments
        params.forEach { arguments?.putParcelable(it.first, it.second) }
    }

    navigate(screen.name, builder)
}

fun NavController.navigate(
    screen: NavSection.Screen,
    params: Bundle? = null,
    builder: NavOptionsBuilder.() -> Unit = { }
) {
    this.currentBackStackEntry?.arguments?.putAll(params)

    navigate(screen.name, builder)
}