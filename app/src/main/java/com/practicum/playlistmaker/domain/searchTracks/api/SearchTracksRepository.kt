package com.practicum.playlistmaker.domain.searchTracks.api

import com.practicum.playlistmaker.domain.searchTracks.models.SearchTracksResult

interface SearchTracksRepository {
    fun searchTracks(
        expression: String
    ): SearchTracksResult
}