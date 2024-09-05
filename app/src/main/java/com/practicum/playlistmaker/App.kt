package com.practicum.playlistmaker

import android.app.Application
import android.content.res.Configuration

const val THEME_KEY = "current_theme"
const val PREFERENCES = "shared_preferences"

class App: Application() {

    var darkTheme = false
    var current_theme_app = "light"
    var current_theme_pref = "light"

    override fun onCreate() {
        super.onCreate()

        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val darkThemeApp = nightModeFlags == Configuration.UI_MODE_NIGHT_YES
        if (darkThemeApp) {
            current_theme_app = "dark"
        }

        val sharedPrefs = getSharedPreferences(PREFERENCES, MODE_PRIVATE)
        darkTheme = (sharedPrefs.getString(THEME_KEY, current_theme_app) == "dark")

        current_theme_pref = when {
            darkTheme -> "dark" else -> "light"
        }
        sharedPrefs.edit()
            .putString(THEME_KEY, current_theme_pref)
            .apply()

    }
}