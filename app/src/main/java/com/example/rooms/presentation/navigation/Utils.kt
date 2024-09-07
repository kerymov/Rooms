package com.example.rooms.presentation.navigation

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder

/**
 * [NavController.navigate] is an extension for [NavController]
 * to navigate between [NavModule]'s and [NavModule.Screen]'s
 * with [Parcelable] params or params in [Bundle]
 */

fun NavController.navigate(
    module: NavModule,
    param: Pair<String, Parcelable>? = null,
    builder: NavOptionsBuilder.() -> Unit = { }
) {
    param?.let { this.currentBackStackEntry?.arguments?.putParcelable(param.first, param.second) }

    navigate(module.route, builder)
}

fun NavController.navigate(
    module: NavModule,
    params: List<Pair<String, Parcelable>>? = null,
    builder: NavOptionsBuilder.() -> Unit = { }
) {
    params?.let {
        val arguments = this.currentBackStackEntry?.arguments
        params.forEach { arguments?.putParcelable(it.first, it.second) }
    }

    navigate(module.route, builder)
}

fun NavController.navigate(
    module: NavModule,
    params: Bundle? = null,
    builder: NavOptionsBuilder.() -> Unit = { }
) {
    this.currentBackStackEntry?.arguments?.putAll(params)

    navigate(module.route, builder)
}

fun NavController.navigate(
    submodule: NavModule.Submodule,
    param: Pair<String, Parcelable>? = null,
    builder: NavOptionsBuilder.() -> Unit = { }
) {
    param?.let { this.currentBackStackEntry?.arguments?.putParcelable(param.first, param.second) }

    navigate(submodule.route, builder)
}

fun NavController.navigate(
    submodule: NavModule.Submodule,
    params: List<Pair<String, Parcelable>>? = null,
    builder: NavOptionsBuilder.() -> Unit = { }
) {
    params?.let {
        val arguments = this.currentBackStackEntry?.arguments
        params.forEach { arguments?.putParcelable(it.first, it.second) }
    }

    navigate(submodule.route, builder)
}

fun NavController.navigate(
    submodule: NavModule.Submodule,
    params: Bundle? = null,
    builder: NavOptionsBuilder.() -> Unit = { }
) {
    this.currentBackStackEntry?.arguments?.putAll(params)

    navigate(submodule.route, builder)
}

fun NavController.navigate(
    screen: NavModule.Screen,
    param: Pair<String, Parcelable>? = null,
    builder: NavOptionsBuilder.() -> Unit = { }
) {
    param?.let { this.currentBackStackEntry?.arguments?.putParcelable(param.first, param.second) }

    navigate(screen.route, builder)
}

fun NavController.navigate(
    screen: NavModule.Screen,
    params: List<Pair<String, Parcelable>>? = null,
    builder: NavOptionsBuilder.() -> Unit = { }
) {
    params?.let {
        val arguments = this.currentBackStackEntry?.arguments
        params.forEach { arguments?.putParcelable(it.first, it.second) }
    }

    navigate(screen.route, builder)
}

fun NavController.navigate(
    screen: NavModule.Screen,
    params: Bundle? = null,
    builder: NavOptionsBuilder.() -> Unit = { }
) {
    this.currentBackStackEntry?.arguments?.putAll(params)

    navigate(screen.route, builder)
}