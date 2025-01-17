package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.SearchTracksResult
import com.practicum.playlistmaker.search.domain.models.Track

interface SearchTracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(result: SearchTracksResult<List<Track>>)
    }
}