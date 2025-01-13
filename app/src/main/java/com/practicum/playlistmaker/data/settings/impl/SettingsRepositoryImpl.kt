package com.practicum.playlistmaker.data.settings.impl

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import com.practicum.playlistmaker.data.settings.SettingsRepository
import com.practicum.playlistmaker.utils.constants.Constants

class SettingsRepositoryImpl(private val sharedPreferences: SharedPreferences): SettingsRepository {
    override fun isThemeInSettingsDark(context: Context): Boolean {
        val nightModeFlags = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return sharedPreferences.getBoolean(Constants.THEME_KEY, nightModeFlags == Configuration.UI_MODE_NIGHT_YES)
        //второй параметр - данные по умолчанию если файла настроек нет
    }

    override fun writeThemeInSettingsDark(setDark: Boolean) {
        sharedPreferences.edit()
            .putBoolean(Constants.THEME_KEY, setDark)
            .apply()
    }
}