package com.practicum.playlistmaker.data.search

import com.practicum.playlistmaker.domain.search.models.SearchTracksResult

interface SearchTracksRepository {
    fun searchTracks(
        expression: String
    ): SearchTracksResult
}