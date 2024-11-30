package com.practicum.playlistmaker.domain.settings.api

import android.content.Context

interface SettingsRepository {
    fun isThemeInSettingsDark(context: Context): Boolean
    fun writeThemeInSettingsDark(setDark: Boolean)
}