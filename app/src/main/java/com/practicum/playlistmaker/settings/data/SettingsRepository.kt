package com.practicum.playlistmaker.settings.data

import android.content.Context

interface SettingsRepository {
    fun isThemeInSettingsDark(): Boolean
    fun writeThemeInSettingsDark(setDark: Boolean)
}