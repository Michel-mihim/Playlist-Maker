package com.practicum.playlistmaker.settings.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.creator.Creator

class SettingsViewModel(application: Application): AndroidViewModel(application) {

    companion object {
        fun getSettingsViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }

    var initialThemeIsDark = false
    val app_context = application as App

    init {
        initialThemeIsDark = app_context.isThemeDarkForChecker
    }

    private val settingsInteractor = Creator.provideSettingsInteractor(getApplication<Application>())

    private val settingsActivityDarkThemeLiveData = MutableLiveData<Boolean>()
    fun observeSettingsActivityTheme(): LiveData<Boolean> = settingsActivityDarkThemeLiveData

    fun initialThemeIsDarkSet() {
        themeSwitcherIsDarkSetter(initialThemeIsDark)
    }

    fun switchTheme(checked: Boolean) {
        Log.d("wtf", "ViewModel switchTheme "+checked.toString())
        app_context.switchTheme(
            settingsInteractor,
            checked
        )
    }

    //POSTING=======================================================================================
    private fun themeSwitcherIsDarkSetter(isDark: Boolean) {
        settingsActivityDarkThemeLiveData.postValue(isDark)
    }
}