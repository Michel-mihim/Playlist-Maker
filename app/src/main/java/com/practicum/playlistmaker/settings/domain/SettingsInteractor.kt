package com.practicum.playlistmaker.settings.domain


interface SettingsInteractor {
    fun isThemeDark(): Boolean
    fun writeThemeDark(setThemeDark: Boolean)
}