package com.practicum.playlistmaker.data

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.utils.constants.Constants

class App: Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        val sharedPrefs = getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE)
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
        return (sharedPrefs.getBoolean(Constants.THEME_KEY, isSysThemeDark()))
    }

    fun writeThemePrefsDark(sharedPrefs: SharedPreferences, darkThemeEnabled: Boolean) {
        sharedPrefs.edit()
            .putBoolean(Constants.THEME_KEY, darkThemeEnabled)
            .apply()
    }
}