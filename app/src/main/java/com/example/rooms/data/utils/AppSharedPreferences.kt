package com.example.rooms.data.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.content.edit

object AppSharedPreferences {

    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences("Rooms_Preferences", MODE_PRIVATE)
    }

    var userName: String?
        get() = Key.USER_NAME.getString()
        set(value) = Key.USER_NAME.putString(value)

    var authToken: String?
        get() = Key.AUTH_TOKEN.getString()
        set(value) = Key.AUTH_TOKEN.putString(value)

    var authTokenExpiresIn: Int?
        get() = Key.AUTH_TOKEN_EXPIRES_IN.getInt()
        set(value) = Key.AUTH_TOKEN_EXPIRES_IN.putInt(value)

    private enum class Key {
        USER_NAME,
        AUTH_TOKEN,
        AUTH_TOKEN_EXPIRES_IN;

        fun getString(): String? = if (sharedPreferences.contains(name)) sharedPreferences.getString(name, "") else null
        fun putString(value: String?) = value?.let { sharedPreferences.edit { putString(name, it) } } ?: remove()

        fun getInt(): Int? = if (sharedPreferences.contains(name)) sharedPreferences.getInt(name, 0) else null
        fun putInt(value: Int?) = value?.let { sharedPreferences.edit { putInt(name, it) } } ?: remove()

        private fun remove() = sharedPreferences.edit { remove(name) }
    }
}