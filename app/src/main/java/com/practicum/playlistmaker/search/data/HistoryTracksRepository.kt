package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.domain.OnHistoryUpdatedListener
import com.practicum.playlistmaker.search.domain.models.Track

interface HistoryTracksRepository {
    fun getTracks(): Array<Track>
    fun addTrack(track: Track)
    fun clearTracks()
    fun SetOnHistoryUpdatedListener(onHistoryUpdatedListener: OnHistoryUpdatedListener)
}