package com.practicum.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson

const val SEARCH_HISTORY_KEY = "history"

class SearchHistory(val sharedPrefs: SharedPreferences) {
    fun writeHistory(tracks: ArrayList<Track>) {
        val json = Gson().toJson(tracks)
        this.sharedPrefs.edit()
            .putString(SEARCH_HISTORY_KEY, json)
            .apply()
    }

    fun readHistory(): Array<Track> {
        val json = this.sharedPrefs.getString(SEARCH_HISTORY_KEY, null) ?: return emptyArray()
        return Gson().fromJson(json, Array<Track>::class.java)
    }

    fun clearHistory() {
        this.sharedPrefs.edit()
            .remove(SEARCH_HISTORY_KEY)
            .apply()
    }

}