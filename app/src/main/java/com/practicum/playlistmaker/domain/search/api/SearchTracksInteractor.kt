package com.practicum.playlistmaker.domain.search.api

import com.practicum.playlistmaker.domain.search.models.SearchTracksResult

interface SearchTracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(result: SearchTracksResult)
    }
}