package com.example.rooms.utils

import android.content.Context
import android.content.SharedPreferences

const val APP_PREFERENCES = "Preferences"

object AppPreferences {
    private var sharedPreferences: SharedPreferences? = null

    fun setup(context: Context) {
        sharedPreferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
    }

    var accessToken: String?
        get() = sharedPreferences?.getString("token", null)
        set(value) {
            sharedPreferences?.edit()?.putString("token", value)?.apply()
        }
}
