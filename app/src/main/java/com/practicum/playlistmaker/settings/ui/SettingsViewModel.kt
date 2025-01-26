package com.practicum.playlistmaker.settings.ui

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
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

    //==============================================================================================
    //SETTINGS======================================================================================
    private val settingsActivityDarkThemeLiveData = MutableLiveData<Boolean>()
    fun observeSettingsActivityTheme(): LiveData<Boolean> = settingsActivityDarkThemeLiveData

    private val app_link = application

    private val settingsInteractor = Creator.provideSettingsInteractor(getApplication<Application>())

    //POSTING=======================================================================================
    private fun themeSwitcherIsDarkSetter(isDark: Boolean) {
        settingsActivityDarkThemeLiveData.postValue(isDark)
    }
    //==============================================================================================

    init {
        themeSwitcherIsDarkSetter(settingsInteractor.isThemeDark(application))
    }


    fun switchTheme(checked: Boolean) {
        (app_link as App).switchTheme(checked)

        settingsInteractor.writeThemeDark(checked)
        themeSwitcherIsDarkSetter(checked)
    }

    //==============================================================================================
    //SHARING=======================================================================================
    private val sharingInteractor = Creator.provideSharingInteractor()

    fun shareApp() {
        sharingInteractor.shareApp(
            onChooserReady = { chooser ->
                startActivity(app_link, chooser as Intent, null)
            }
        )
    }

}