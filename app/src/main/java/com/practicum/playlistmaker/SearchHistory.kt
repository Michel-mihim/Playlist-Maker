package com.practicum.playlistmaker

import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson

const val SEARCH_HISTORY_KEY = "history"

class SearchHistory(val sharedPrefs: SharedPreferences) {

    private var newTracks = ArrayList<Track>()

    fun writeHistory(trackAdded: Track) {

        val lastTracks = readHistory()
        newTracks.clear()
        newTracks.addAll(historyNewReady(trackAdded, lastTracks, 10))

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

    fun historyNewReady(trackAdded: Track, tracks: Array<Track>, maxTracksCount: Int): ArrayList<Track> {
        var tracksNewReady = ArrayList<Track>()
        if (trackListChecker(getTrackId(trackAdded), tracks)) {
            Log.d("WTF", "Есть такой. Будет перемещен на первое место")
            return moveTrackIfPresent(trackAdded, tracks)
        }
        Log.d("WTF", "Нет такого. Будет добавлен")
        return addTrackIfAbsent(trackAdded, tracks)
        }

    fun getTrackId(track: Track): String {
        return track.trackId
    }

    fun trackListChecker(trackId: String, tracks: Array<Track>): Boolean {
        for (track in tracks) {
            if (getTrackId(track) == trackId) {
                return true
            }
        }
        return false
    }

    fun addTrackIfAbsent(trackAdded: Track, tracks: Array<Track>): ArrayList<Track> {
        var tracksReady = ArrayList<Track>()
        tracksReady.add(trackAdded)
        tracksReady.addAll(tracks)
        return tracksReady
    }

    fun moveTrackIfPresent(trackAdded: Track, tracks: Array<Track>): ArrayList<Track> {

        return
    }
}