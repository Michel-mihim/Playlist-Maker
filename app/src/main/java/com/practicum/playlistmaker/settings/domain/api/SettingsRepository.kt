package com.practicum.playlistmaker.settings.domain.api

interface SettingsRepository {
    fun isThemeInSettingsDark(): Boolean
    fun writeThemeInSettingsDark(setDark: Boolean)
}