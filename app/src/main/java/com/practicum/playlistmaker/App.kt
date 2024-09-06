package com.practicum.playlistmaker

import android.app.Application
import android.content.res.Configuration
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import kotlin.math.log


const val THEME_KEY = "night_theme"
const val PREFERENCES = "shared_preferences"

class App: Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        val sharedPrefs = getSharedPreferences(PREFERENCES, MODE_PRIVATE)
        darkTheme = (sharedPrefs.getString(THEME_KEY, isSysThemeDark().toString())).toBoolean()

        Log.d("SysDarkPrefsDark", isSysThemeDark().toString()+" "+darkTheme.toString())

        //sharedPrefs.edit()
        //    .putString(THEME_KEY, isSysThemeDark().toString())
        //    .apply()

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

    fun isSysThemeDark(): Boolean {
        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES
    }
}