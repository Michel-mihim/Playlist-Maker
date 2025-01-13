package com.practicum.playlistmaker.domain.history.api

import com.practicum.playlistmaker.domain.history.OnHistoryUpdatedListener
import com.practicum.playlistmaker.domain.search.models.Track

interface HistoryTracksInteractor {
    fun getTracks(): Array<Track>
    fun addTrack(track: Track)
    fun clearTracks()
    fun SetOnHistoryUpdatedListener(onHistoryUpdatedListener: OnHistoryUpdatedListener)
}