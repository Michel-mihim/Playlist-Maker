package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.SearchTracksResult
import com.practicum.playlistmaker.domain.models.Track

interface TracksRepository {
    fun searchTracks(
        expression: String
    ): SearchTracksResult

    fun writeTrack(trackClicked: Track)
}