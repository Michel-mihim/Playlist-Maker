package com.practicum.playlistmaker

import android.app.Application
import android.content.res.Configuration
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate


const val THEME_KEY = "night_theme"
const val PREFERENCES = "shared_preferences"

class App: Application() {

    var darkTheme = false


    override fun onCreate() {
        super.onCreate()

        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val darkThemeSys = nightModeFlags == Configuration.UI_MODE_NIGHT_YES

        }

        val sharedPrefs = getSharedPreferences(PREFERENCES, MODE_PRIVATE)
        darkTheme = (sharedPrefs.getString(THEME_KEY, current_theme_app) == "dark")

        current_theme_pref = when {
            darkTheme -> "dark" else -> "light"
        }
        sharedPrefs.edit()
            .putString(THEME_KEY, current_theme_pref)
            .apply()

        Log.d("App", "Я выполнилось")
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}