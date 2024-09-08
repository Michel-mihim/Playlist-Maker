package com.practicum.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson

const val SEARCH_HISTORY_KEY = "history"

class SearchHistory(sharedPrefs: SharedPreferences) {

    fun writeHistory(sharedPrefs: SharedPreferences, track: Track) {
        val json = Gson().toJson(track)
        sharedPrefs.edit()
            .putString(SEARCH_HISTORY_KEY, json)
            .apply()
    }

    fun readHistory(sharedPrefs: SharedPreferences):Track? {
        val json = sharedPrefs.getString(SEARCH_HISTORY_KEY, null) ?: return null
        return Gson().fromJson(json, Track::class.java)
    }

}