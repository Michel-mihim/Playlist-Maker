package com.practicum.playlistmaker.settings.data

import android.content.Context

interface SettingsRepository {
    fun isThemeInSettingsDark(context: Context): Boolean
    fun writeThemeInSettingsDark(setDark: Boolean)
}