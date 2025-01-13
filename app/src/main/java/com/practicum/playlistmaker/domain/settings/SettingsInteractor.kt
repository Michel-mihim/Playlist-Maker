package com.practicum.playlistmaker.domain.settings

import android.content.Context

interface SettingsInteractor {
    fun isThemeDark(context: Context): Boolean
    fun writeThemeDark(setThemeDark: Boolean)
}