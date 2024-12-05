package com.practicum.playlistmaker.domain.history.api

import com.practicum.playlistmaker.domain.history.listener.OnHistoryUpdatedListener
import com.practicum.playlistmaker.domain.searchTracks.models.Track

interface HistoryTracksInteractor {
    fun getTracks(): Array<Track>
    fun addTrack(track: Track)
    fun clearTracks()

    fun setOnHistoryUpdatedListener(onHistoryUpdatedListener: OnHistoryUpdatedListener)
}