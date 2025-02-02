package com.practicum.playlistmaker.settings.domain.api


interface SettingsInteractor {
    fun isThemeDark(): Boolean
    fun writeThemeDark(setThemeDark: Boolean)
}