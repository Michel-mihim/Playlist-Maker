package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.OnHistoryUpdatedListener
import com.practicum.playlistmaker.search.domain.models.Track

interface HistoryTracksRepository {
    fun getTracks(): Array<Track>
    fun addTrack(track: Track)
    fun clearTracks()
    fun setOnHistoryUpdatedListener(onHistoryUpdatedListener: OnHistoryUpdatedListener)
}