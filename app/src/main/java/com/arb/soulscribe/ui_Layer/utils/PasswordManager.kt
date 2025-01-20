package com.arb.soulscribe.utils

import android.content.Context

object PasswordManager {
    private const val PREF_NAME = "password_prefs"
    private const val KEY_PASSWORD = "app_password"

    fun getPassword(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_PASSWORD, null)
    }

    fun setPassword(context: Context, password: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_PASSWORD, password).apply()
    }

    fun isPasswordSet(context: Context): Boolean {
        return getPassword(context) != null
    }
}
