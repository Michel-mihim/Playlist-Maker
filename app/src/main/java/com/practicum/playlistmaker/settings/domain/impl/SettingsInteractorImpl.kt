package com.practicum.playlistmaker.settings.domain.impl

import android.content.Context
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.data.SettingsRepository

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository):
    SettingsInteractor {
    override fun isThemeDark(context: Context): Boolean {
        return settingsRepository.isThemeInSettingsDark(context)
    }

    override fun writeThemeDark(setThemeDark: Boolean) {
        settingsRepository.writeThemeInSettingsDark(setThemeDark)
    }
}