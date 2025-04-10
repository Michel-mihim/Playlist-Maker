package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.di.dataModule
import com.practicum.playlistmaker.di.interactorModule
import com.practicum.playlistmaker.di.repositoryModule
import com.practicum.playlistmaker.di.viewModelModule
import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {

    var isThemeDarkFlag = false

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }

        val settingsInteractor: SettingsInteractor by inject<SettingsInteractor>()

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