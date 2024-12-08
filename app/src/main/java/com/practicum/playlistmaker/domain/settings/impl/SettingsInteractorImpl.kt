package com.practicum.playlistmaker.domain.settings.impl

import android.content.Context
import com.practicum.playlistmaker.data.settings.SettingsRepositoryImpl
import com.practicum.playlistmaker.domain.settings.api.SettingsInteractor
import com.practicum.playlistmaker.domain.settings.api.SettingsRepository

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository): SettingsInteractor {
    override fun isThemeDark(context: Context): Boolean {
        return settingsRepository.isThemeInSettingsDark(context)
    }

    override fun writeThemeDark(setThemeDark: Boolean) {
        settingsRepository.writeThemeInSettingsDark(setThemeDark)
    }
}