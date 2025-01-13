package com.practicum.playlistmaker.domain.settings.impl

import android.content.Context
import com.practicum.playlistmaker.domain.settings.SettingsInteractor
import com.practicum.playlistmaker.data.settings.SettingsRepository

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository):
    SettingsInteractor {
    override fun isThemeDark(context: Context): Boolean {
        return settingsRepository.isThemeInSettingsDark(context)
    }

    override fun writeThemeDark(setThemeDark: Boolean) {
        settingsRepository.writeThemeInSettingsDark(setThemeDark)
    }
}