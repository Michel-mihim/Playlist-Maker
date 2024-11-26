package com.practicum.playlistmaker.domain.searchTracks.api

import com.practicum.playlistmaker.domain.searchTracks.models.SearchTracksResult

interface SearchTracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(result: SearchTracksResult)
    }
}