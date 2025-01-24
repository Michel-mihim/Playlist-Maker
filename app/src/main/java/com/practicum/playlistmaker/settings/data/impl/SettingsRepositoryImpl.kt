package com.practicum.playlistmaker.settings.data.impl

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.settings.data.SettingsRepository
import com.practicum.playlistmaker.utils.constants.Constants

class SettingsRepositoryImpl(private val sharedPreferences: SharedPreferences): SettingsRepository {

    override fun isThemeInSettingsDark(context: Context): Boolean {
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