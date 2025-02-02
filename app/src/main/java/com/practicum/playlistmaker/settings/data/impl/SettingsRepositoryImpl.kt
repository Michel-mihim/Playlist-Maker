package com.practicum.playlistmaker.settings.data.impl

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import com.practicum.playlistmaker.settings.domain.api.SettingsRepository
import com.practicum.playlistmaker.utils.constants.Constants

class SettingsRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val context: Context
    ): SettingsRepository {

    override fun isThemeInSettingsDark(): Boolean {
        val nightModeFlags = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return sharedPreferences.getBoolean(Constants.THEME_KEY, nightModeFlags == Configuration.UI_MODE_NIGHT_YES)
        //первый параметр - тема, прописанная в настройках,
        //второй параметр - данные по умолчанию если файла настроек нет, то текущая тема системы будет передана в ответ
    }

    override fun writeThemeInSettingsDark(setDark: Boolean) {
        sharedPreferences.edit()
            .putBoolean(Constants.THEME_KEY, setDark)
            .apply()
    }
}