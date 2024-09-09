package com.practicum.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson

const val SEARCH_HISTORY_KEY = "history"

class SearchHistory(val sharedPrefs: SharedPreferences) {
    fun writeHistory(track: Track) {
        val json = Gson().toJson(track)
        this.sharedPrefs.edit()
            .putString(SEARCH_HISTORY_KEY, json)
            .apply()
    }

    fun readHistory():Track? {
        val json = this.sharedPrefs.getString(SEARCH_HISTORY_KEY, null) ?: return null
        return Gson().fromJson(json, Track::class.java)
    }

    fun clearHistory() {
        this.sharedPrefs.edit()
            .remove(SEARCH_HISTORY_KEY)
            .apply()
    }

}