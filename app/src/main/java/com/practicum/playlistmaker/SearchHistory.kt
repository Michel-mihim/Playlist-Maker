package com.practicum.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson

const val SEARCH_HISTORY_KEY = "history"
const val HISTORY_CAPACITY = 10

class SearchHistory(val sharedPrefs: SharedPreferences) {

    private var newTracks = ArrayList<Track>()

    fun writeHistory(trackAdded: Track) {

        val lastTracks = readHistory()
        newTracks.clear()
        newTracks.addAll(historyNewGenerator(trackAdded, lastTracks, HISTORY_CAPACITY))

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

    fun historyNewGenerator(trackAdded: Track, tracks: Array<Track>, maxTracksCount: Int): ArrayList<Track> {
        var newTracks = ArrayList<Track>()
        trackChecker(
            trackAdded, tracks,
            onPresent = { id ->   newTracks.addAll(makeFirstTrackIfPresent(trackAdded, tracks))},
            onAbsent = { newTracks.addAll(addTrackIfAbsent(trackAdded, tracks)) }
        )
        return newTracks.take(maxTracksCount).toCollection(ArrayList<Track>())
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

    fun getDuplicateListId(trackAdded: Track, tracks: Array<Track>): Int {
        for (i in 0..tracks.size) {
            if (getTrackId(tracks[i]) == trackAdded.trackId) {
                return i
            }
        }
        return 0
    }

    fun trackChecker(trackAdded: Track, tracks: Array<Track>,
        onAbsent: (Boolean) -> Unit,
        onPresent: (Int) -> Unit
    ) {
        if (trackListChecker(getTrackId(trackAdded), tracks)) {
            onPresent(getDuplicateListId(trackAdded, tracks))
        } else {
            onAbsent(false)
        }
    }

    fun addTrackIfAbsent(trackAdded: Track, tracks: Array<Track>): ArrayList<Track> {
        var tracksReady = ArrayList<Track>()
        tracksReady.add(trackAdded)
        tracksReady.addAll(tracks)
        return tracksReady
    }

    fun makeFirstTrackIfPresent(trackAdded: Track, tracks: Array<Track>): ArrayList<Track> {
        var tracksReady = ArrayList<Track>()
        var tracksList: MutableList<Track> = ArrayList()
        tracksList.addAll(tracks)
        tracksList.removeAt(getDuplicateListId(trackAdded, tracks))

        tracksReady.add(trackAdded)
        tracksReady.addAll(tracksList)

        return tracksReady
    }


}