package com.practicum.playlistmaker.data.settings

import android.content.Context

interface SettingsRepository {
    fun isThemeInSettingsDark(context: Context): Boolean
    fun writeThemeInSettingsDark(setDark: Boolean)
}