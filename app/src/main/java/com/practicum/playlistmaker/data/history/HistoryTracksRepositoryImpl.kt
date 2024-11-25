package com.practicum.playlistmaker.data.history

import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.domain.history.api.HistoryTracksRepository
import com.practicum.playlistmaker.domain.searchTracks.models.Track
import com.practicum.playlistmaker.presentation.Constants

class HistoryTracksRepositoryImpl(private val sharedPrefs: SharedPreferences) : HistoryTracksRepository {

    private var newTracks = ArrayList<Track>()

    override fun getTracks(): Array<Track> {
        val json = sharedPrefs.getString(Constants.SEARCH_HISTORY_KEY, null) ?: return emptyArray()
        return Gson().fromJson(json, Array<Track>::class.java)
    }

    override fun addTrack(track: Track) {
        val lastTracks = getTracks()
        newTracks.clear()
        newTracks.addAll()
    }

    override fun clearTracks() {
        TODO("Not yet implemented")
    }

}