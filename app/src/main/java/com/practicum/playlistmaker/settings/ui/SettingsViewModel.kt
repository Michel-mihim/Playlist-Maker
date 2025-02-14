package com.practicum.playlistmaker.settings.ui

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor
import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor,
    //private val app_link: Application
): ViewModel() {


    //==============================================================================================
    //SETTINGS======================================================================================
    private val settingsActivityDarkThemeLiveData = MutableLiveData<Boolean>()
    fun observeSettingsActivityTheme(): LiveData<Boolean> = settingsActivityDarkThemeLiveData

    //POSTING=======================================================================================
    private fun themeSwitcherIsDarkSetter(isDark: Boolean) {
        settingsActivityDarkThemeLiveData.postValue(isDark)
    }
    //==============================================================================================

    init {
        themeSwitcherIsDarkSetter(settingsInteractor.isThemeDark())
    }

    fun switchTheme(checked: Boolean) {
        //(app_link as App).switchTheme(checked)

        settingsInteractor.writeThemeDark(checked)
        themeSwitcherIsDarkSetter(checked)
    }

    //==============================================================================================
    //SHARING=======================================================================================

    private val shareActivityIntentLiveData = MutableLiveData<Intent>()
    fun observeShareActivityIntentLiveData(): LiveData<Intent> = shareActivityIntentLiveData

    private val supportEmailActivityIntentLiveData = MutableLiveData<Intent>()
    fun observeSupportEmailActivityIntentLiveData(): LiveData<Intent> = supportEmailActivityIntentLiveData

    private val termsIntentLiveData = MutableLiveData<Intent>()
    fun observeTermsIntentLiveData(): LiveData<Intent> = termsIntentLiveData

    fun shareApp() {
        sharingInteractor.shareApp(
            onChooserReady = { intent ->
                startShareActivity(intent as Intent)
            }
        )
    }

    fun openSupport() {
        sharingInteractor.openSupport(
            onSupportEmailIntentReady = { intent ->
                startSupportEmailActivity(intent as Intent)
            }
        )
    }

    fun openTerms() {
        sharingInteractor.openTerms(
            onTermsIntentReady = { intent ->
                startTermsIntentActivity(intent as Intent)
            }
        )
    }

    //POSTING=======================================================================================
    private fun startShareActivity(intent: Intent) {
        shareActivityIntentLiveData.postValue(intent)
    }

    private fun startSupportEmailActivity(intent: Intent) {
        supportEmailActivityIntentLiveData.postValue(intent)
    }

    private fun startTermsIntentActivity(intent: Intent) {
        termsIntentLiveData.postValue(intent)
    }

}