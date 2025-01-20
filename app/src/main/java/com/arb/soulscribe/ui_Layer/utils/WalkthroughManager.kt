package com.arb.soulscribe.ui_Layer.utils

import android.content.Context

object WalkthroughManager {
    private const val PREF_NAME = "walkthrough_prefs"
    private const val KEY_WALKTHROUGH_SHOWN = "is_walkthrough_shown"

    fun isWalkthroughShown(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_WALKTHROUGH_SHOWN, false)
    }

    fun setWalkthroughShown(context: Context)   {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_WALKTHROUGH_SHOWN, true).apply()
    }
}
