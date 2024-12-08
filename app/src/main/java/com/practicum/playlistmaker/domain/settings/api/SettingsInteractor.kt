package com.practicum.playlistmaker.domain.settings.api

import android.content.Context

interface SettingsInteractor {
    fun isThemeDark(context: Context): Boolean
    fun writeThemeDark(setThemeDark: Boolean)
}