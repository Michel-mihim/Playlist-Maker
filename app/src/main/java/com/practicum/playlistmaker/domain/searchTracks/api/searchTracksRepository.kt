package com.practicum.playlistmaker.domain.searchTracks.api

import com.practicum.playlistmaker.domain.searchTracks.models.SearchTracksResult

interface searchTracksRepository {
    fun searchTracks(
        expression: String
    ): SearchTracksResult
}