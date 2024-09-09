package com.practicum.playlistmaker

import android.content.SharedPreferences
import android.widget.Toast
import com.google.gson.Gson

const val SEARCH_HISTORY_KEY = "history"

class SearchHistory(val sharedPrefs: SharedPreferences) {

    private var newTracks = ArrayList<Track>()

    fun writeHistory(trackAdded: Track) {

        val lastTracks = readHistory()
        newTracks.clear()
        newTracks.addAll(historyVerifying(trackAdded, lastTracks, 10))

        val json = Gson().toJson(newTracks)
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

    fun historyVerifying(trackAdded: Track, tracks: Array<Track>, maxTracksCount: Int): ArrayList<Track> {

        var idsList = ArrayList<String>()
        tracks.forEach { track ->
           idsList.add(track.trackId)
        }

        if (trackAdded.trackId in idsList) {

        } else {

        }
        return
    }

    fun isInList():

}