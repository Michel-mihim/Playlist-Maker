package com.practicum.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.domain.searchTracks.models.Track
import com.practicum.playlistmaker.utils.constants.Constants


class SearchHistory(private val sharedPrefs: SharedPreferences) {

    private var newTracks = ArrayList<Track>()

    fun writeHistory(trackClicked: Track) {

        val lastTracks = readHistory()
        newTracks.clear()
        newTracks.addAll(historyNewGenerator(trackClicked, lastTracks))

        val json = Gson().toJson(newTracks)
        this.sharedPrefs.edit()
            .putString(Constants.SEARCH_HISTORY_KEY, json)
            .apply()
    }

    fun readHistory(): Array<Track> {
        val json = this.sharedPrefs.getString(Constants.SEARCH_HISTORY_KEY, null) ?: return emptyArray()
        return Gson().fromJson(json, Array<Track>::class.java)
    }

    fun clearHistory() {
        this.sharedPrefs.edit()
            .remove(Constants.SEARCH_HISTORY_KEY)
            .apply()
    }

    private fun historyNewGenerator(trackAdded: Track, tracks: Array<Track>): ArrayList<Track> {
        val newTracks = ArrayList<Track>()
        trackChecker(
            trackAdded, tracks,
            onPresent = { _ ->   newTracks.addAll(makeFirstTrackIfPresent(trackAdded, tracks))},
            onAbsent = { newTracks.addAll(addTrackIfAbsent(trackAdded, tracks)) }
        )
        return newTracks.take(Constants.HISTORY_CAPACITY).toCollection(ArrayList())
        }

    private fun getTrackId(track: Track): String {
        return track.trackId
    }

    private fun trackListChecker(trackId: String, tracks: Array<Track>): Boolean {
        for (track in tracks) {
            if (getTrackId(track) == trackId) {
                return true
            }
        }
        return false
    }

    private fun getDuplicateListId(trackAdded: Track, tracks: Array<Track>): Int {
        for (i in 0..tracks.size) {
            if (getTrackId(tracks[i]) == trackAdded.trackId) {
                return i
            }
        }
        return 0
    }

    private fun trackChecker(trackAdded: Track, tracks: Array<Track>,
                             onAbsent: (Boolean) -> Unit,
                             onPresent: (Int) -> Unit
    ) {
        if (trackListChecker(getTrackId(trackAdded), tracks)) {
            onPresent(getDuplicateListId(trackAdded, tracks))
        } else {
            onAbsent(false)
        }
    }

    private fun addTrackIfAbsent(trackAdded: Track, tracks: Array<Track>): ArrayList<Track> {
        val tracksReady = ArrayList<Track>()
        tracksReady.add(trackAdded)
        tracksReady.addAll(tracks)
        return tracksReady
    }

    private fun makeFirstTrackIfPresent(trackAdded: Track, tracks: Array<Track>): ArrayList<Track> {
        val tracksReady = ArrayList<Track>()
        val tracksList: MutableList<Track> = ArrayList()
        tracksList.addAll(tracks)
        tracksList.removeAt(getDuplicateListId(trackAdded, tracks))

        tracksReady.add(trackAdded)
        tracksReady.addAll(tracksList)

        return tracksReady
    }


}