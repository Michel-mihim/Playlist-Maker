package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.SearchTracksResult
import com.practicum.playlistmaker.domain.models.Track

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(result: SearchTracksResult)
    }

    fun writeHistoryTracks(trackClicked: Track)
}