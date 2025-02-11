package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.di.dataModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {

    var isThemeDarkFlag = false

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(dataModule)
        }

        val settingsInteractor = Creator.provideSettingsInteractor(this)

        isThemeDarkFlag = settingsInteractor.isThemeDark() //тема либо из настроек, либо системная

        switchTheme(isThemeDarkFlag) //установим для всего приложения полученный результат

        settingsInteractor.writeThemeDark(isThemeDarkFlag) //заодно запишем/перепишем тему в файл настроек
    }

    fun switchTheme(setDarkTheme: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (setDarkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )

    }

}