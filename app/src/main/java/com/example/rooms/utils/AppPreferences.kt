package com.example.rooms.utils

import android.content.Context
import android.content.SharedPreferences

const val APP_PREFERENCES = "Preferences"

object AppPreferences {
    private lateinit var sharedPreferences: SharedPreferences

    fun setup(context: Context) {
        sharedPreferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
    }

    var username: String?
        get() = sharedPreferences.getString("username", null)
        set(value) {
            sharedPreferences.edit()?.putString("username", value)?.apply()
        }

    var accessToken: String?
        get() = sharedPreferences.getString("token", null)
        set(value) {
            sharedPreferences.edit()?.putString("token", value)?.apply()
        }

    var expiresIn: Int
        get() = sharedPreferences.getInt("expiresIn", 0)
        set(value) {
            sharedPreferences.edit()?.putInt("expiresIn", value)?.apply()
        }
}
