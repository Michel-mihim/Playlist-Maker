package com.practicum.playlistmaker.settings.domain

import android.content.Context

interface SettingsInteractor {
    fun isThemeDark(context: Context): Boolean
    fun writeThemeDark(setThemeDark: Boolean)
}