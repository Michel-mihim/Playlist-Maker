package com.practicum.playlistmaker

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.settings.domain.SettingsInteractor

class App: Application() {

    var isThemeDarkForChecker = false

    override fun onCreate() {
        super.onCreate()

        val settingsInteractor = Creator.provideSettingsInteractor(this)

        isThemeDarkForChecker = readThemePrefsDark(settingsInteractor)

        switchTheme(settingsInteractor, isThemeDarkForChecker)
    }

    fun readThemePrefsDark(settingsInteractor: SettingsInteractor): Boolean {
        return settingsInteractor.isThemeDark(this)
    }

    fun switchTheme(settingsInteractor: SettingsInteractor, darkThemeEnabled: Boolean) {
        Log.d("wtf", "App switchTheme "+darkThemeEnabled.toString())
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        isThemeDarkForChecker = darkThemeEnabled            //актуализируем значение на какую тему переключились
        Log.d("wtf", "App isThemeDarkForChecker, writeThemeDark = "+darkThemeEnabled.toString())
        settingsInteractor.writeThemeDark(darkThemeEnabled) //заодно запишем/перепишем тему в файл настроек
    }

}