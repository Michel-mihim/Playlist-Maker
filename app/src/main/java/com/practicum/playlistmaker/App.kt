package com.practicum.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate


const val THEME_KEY = "night_theme"
const val PREFERENCES = "shared_preferences"

class App: Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        val sharedPrefs = getSharedPreferences(PREFERENCES, MODE_PRIVATE)
        darkTheme = readThemePrefsDark(sharedPrefs)
        switchTheme(sharedPrefs, darkTheme)

    }

    fun switchTheme(sharedPrefs: SharedPreferences, darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        writeThemePrefsDark(sharedPrefs, darkThemeEnabled)
    }

    fun isSysThemeDark(): Boolean {
        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES
    }

    fun readThemePrefsDark(sharedPrefs: SharedPreferences): Boolean {
        return (sharedPrefs.getBoolean(THEME_KEY, isSysThemeDark()))
    }

    fun writeThemePrefsDark(sharedPrefs: SharedPreferences, darkThemeEnabled: Boolean) {
        sharedPrefs.edit()
            .putBoolean(THEME_KEY, darkThemeEnabled)
            .apply()
    }
}